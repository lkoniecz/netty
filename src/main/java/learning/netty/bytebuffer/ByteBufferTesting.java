package learning.netty.bytebuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ByteBufferTesting {

    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8);
        int bytes = buffer.readableBytes();
        System.out.println("bytes: " + bytes);
        for (int i = 0; i < buffer.capacity(); i ++) {
            byte b = buffer.readByte();
            System.out.println((char) b);
            System.out.println(buffer.writerIndex() + " " + buffer.readerIndex());
        }

        System.out.println(buffer.capacity());
        System.out.println("reader index: " + buffer.readerIndex());
    }
}
