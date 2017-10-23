package learning.netty.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import learning.netty.decoder.ByteToUserDecoder;
import learning.netty.encoder.UserToByteEncoder;
import learning.netty.handler.inbound.DefaultInboundHandler;
import learning.netty.handler.inbound.UserInboundHandler;
import learning.netty.handler.outbound.LastOutboundHandler;
import learning.netty.handler.outbound.LukaszOutboundHandler;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); //1
            b.group(group) //2
                    .channel(NioServerSocketChannel.class) //2
                    .localAddress(new InetSocketAddress(port)) //2
                    .childHandler(new ChannelInitializer<SocketChannel>() { //3
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LastOutboundHandler())
                                    .addLast(new UserToByteEncoder())
                                    .addLast(new LukaszOutboundHandler())
                                    .addLast(new DefaultInboundHandler())
                                    .addLast(new ByteToUserDecoder())
                                    .addLast(new UserInboundHandler());
                        }
                    });
            ChannelFuture f = b.bind().sync(); //5
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress()); //6 and 7
            f.channel().closeFuture().sync(); //8
        } finally { //9
            group.shutdownGracefully().sync(); //10
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(8888).start();
    }
}
