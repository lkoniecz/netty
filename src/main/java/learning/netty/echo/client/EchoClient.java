package learning.netty.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
            Bootstrap bootstrap = new Bootstrap(); //1
            bootstrap.group(group) //2
                    .channel(NioSocketChannel.class) //3
                    .remoteAddress(new InetSocketAddress(host, port)) //4
                    .handler(new ChannelInitializer<SocketChannel>() { //5
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler()); //6
                        }
                    });
            ChannelFuture f = bootstrap.connect().sync(); //7
            ByteBuf message = Unpooled.copiedBuffer("jebsie".getBytes());
            f.channel().writeAndFlush(message).addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("Data written successfully");
                } else {
                    System.out.println("Data failed to write:");
                    future.cause().printStackTrace();
                }
            });
            System.out.println("wyslalem");
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