package learning.netty.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); //1
            b.group(group) //2
                    .channel(NioSocketChannel.class) //3
                    .remoteAddress(new InetSocketAddress(host, port)) //4
                    .handler(new ChannelInitializer<SocketChannel>() { //5
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler()); //6
                        }
                    });
            ChannelFuture f = b.connect().sync(); //7
            f.channel().closeFuture().sync(); //8
        } finally {
            group.shutdownGracefully().sync(); //9
        }
    }

    public static void main(String[] args) throws Exception {
        final String host = "localhost";
        final int port = 8888;
        new EchoClient(host, port).start();
    }
}