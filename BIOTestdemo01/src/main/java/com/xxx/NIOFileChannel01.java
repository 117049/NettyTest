package com.xxx;
/**
 * description: Netty 输出文件流
 * author: xhs
 * date: 2022/3/31
 * version:
 **/
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {

        String str = "hello, world";
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");

        FileChannel fileChannel = fileOutputStream.getChannel();
        ByteBuffer byteChannel = ByteBuffer.allocate(1024);

        byteChannel.put(str.getBytes());
        byteChannel.flip();

        fileChannel.write(byteChannel);
        fileOutputStream.close();

    }
}
