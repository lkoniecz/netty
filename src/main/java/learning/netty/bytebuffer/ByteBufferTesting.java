package learning.netty.bytebuffer;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ByteBufferTesting {

    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8);
        buffer.writeBytes(" chuj".getBytes(CharsetUtil.UTF_8));
        for (int i = 0; i < buffer.capacity(); i ++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }

        System.out.println(buffer.capacity());

        System.out.println("reader index: " + buffer.readerIndex());
    }
}
