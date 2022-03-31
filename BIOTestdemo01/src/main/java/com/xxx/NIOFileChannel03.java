package com.xxx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * description: 使用一个buffer对文件进行读写
 * author: xhs
 * date: 2022/3/31
 * version:
 **/
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("E:\\研究生工作\\java_code_new\\NettyTest\\BIOTestdemo01\\src\\NIOFileChannel03TXT.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("E:\\研究生工作\\java_code_new\\NettyTest\\BIOTestdemo01\\src\\NIOFileChannel03TXTOUT.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while(true){
            // 清空数据
            byteBuffer.clear();
            // 将buffer中的数据写出输出流
            int read = fileChannel01.read(byteBuffer);
            if(read == -1){
                break;
            }
            // 将buffer中的数据写入输出流
            byteBuffer.flip();
            int write = fileChannel02.write(byteBuffer);
        }
        fileChannel01.close();
        fileChannel02.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
