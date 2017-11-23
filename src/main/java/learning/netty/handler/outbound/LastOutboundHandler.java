package learning.netty.handler.outbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class LastOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("LastOutboundHandler server received: " + msg + " for channel " + ctx.channel());
        ctx.writeAndFlush(msg).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Data written successfully");
            } else {
                System.out.println("Data failed to write:");
                future.cause().printStackTrace();
            }
        });
    }
}
