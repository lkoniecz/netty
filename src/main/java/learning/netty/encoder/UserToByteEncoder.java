package learning.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import learning.netty.model.User;

public class UserToByteEncoder extends MessageToByteEncoder<User> {

    @Override
    protected void encode(ChannelHandlerContext ctx, User msg, ByteBuf out) throws Exception {
        System.out.println("UserToByteEncoder server received: " + msg + " for channel " + ctx.channel());
        byte[] bytes = msg.getFirstName().getBytes();
        out.writeBytes(bytes);
    }
}
