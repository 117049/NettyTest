package com.xxx;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * description: 使用MappedBuffer 可以让文件直接在内存修改，操作系统不需要进行拷贝
 * author: xhs
 * date: 2022/3/31
 * version:
 **/
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("E:\\研究生工作\\java_code_new\\NettyTest\\BIOTestdemo01\\src\\NIOFileChannel03TXT.txt"
                , "rw");
        // 获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数1 ： FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2 ： 可以直接修改的起始位置
         * 参数3 ： 映射到内存的大小，即将.txt 的多少个字节映射到内存, 5 最多修改五个字节范围内
         **/
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) 'H');
        map.put(3, (byte) '9');

        randomAccessFile.close();
    }
}
