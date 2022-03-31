package com.xxx;

import java.nio.ByteBuffer;

/**
 * description: Netty的buffer文件类型问题
 * author: xhs
 * date: 2022/3/31
 * version:
 **/
public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byteBuffer.putInt(100);
        byteBuffer.putShort((short) 9);
        byteBuffer.putChar('啊');
        byteBuffer.putLong((long) 9999999999999999L);

        byteBuffer.flip();

        System.out.println();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getShort());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getInt());
    }
}
