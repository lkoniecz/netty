package learning.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import learning.netty.model.User;

import java.util.List;
import java.util.stream.Stream;

public class ByteToUserDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ByteToUserDecoder server received: " + in + " for channel " + ctx.channel());
        int readableBytes = in.readableBytes();
        if (readableBytes < 4) {
            return;
        }

        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        Stream.of(new String(bytes).split(" ")).forEach(name -> out.add(new User(name)));
    }
}
