package com.xxx;

import java.nio.IntBuffer;

public class BufferTest {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);
        for(int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put( i * 2);
        }
        //将 buffer 转换，读写切换(!!!)
        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
