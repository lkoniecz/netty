package learning.netty.handler.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class DefaultInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("DefaultInboundHandler server received: " + msg + " for channel " + ctx.channel());
        System.out.println("In event loop " + ctx.channel().eventLoop().inEventLoop());
        ctx.channel().eventLoop().execute(() -> System.out.println("SIEEEEMAAAA"));
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("channelReadComplete");
        //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE); //3
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace(); //4
        ctx.close(); //5
    }
}
