package com.xxx;
/**
 * description: Netty 输入文件流
 * author: xhs
 * date: 2022/3/31
 * version:
 **/

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {
        File file = new File("d:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer allocate = ByteBuffer.allocate((int) file.length());
        int read = fileChannel.read(allocate);
        System.out.println(new String(allocate.array()));
        fileInputStream.close();
    }
}
