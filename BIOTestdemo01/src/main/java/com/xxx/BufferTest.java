package com.xxx;
/**
 * description: Netty Buffer缓冲测试
 * author: xhs 
 * date: 2022/3/31
 * version: 
 **/
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
