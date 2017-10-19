package learning.netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("EchoServerHandler server received: " + msg + " for channel " + ctx.channel());
     //   ctx.writeAndFlush(msg);
      //  ctx.pipeline().remove(this);
      //  ctx.pipeline().fireChannelRead(msg);
        //ctx.write(msg);
        ByteBuf copy = ((ByteBuf) msg).copy(0, 2);
        ctx.fireChannelRead(copy);
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
