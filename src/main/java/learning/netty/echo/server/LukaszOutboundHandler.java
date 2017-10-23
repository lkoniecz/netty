package learning.netty.echo.server;

import io.netty.channel.*;

public class LukaszOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("LukaszOutboundHandler server received: " + msg + " for channel " + ctx.channel());
        ctx.write(msg);
    }
}
