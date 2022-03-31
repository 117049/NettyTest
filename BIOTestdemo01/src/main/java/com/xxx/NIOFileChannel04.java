package com.xxx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * description: 拷贝文件到指定文件夹
 * author: xhs
 * date: 2022/3/31
 * version:
 **/
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("d:\\a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\b.jpg");

        FileChannel fileChannel01 = fileInputStream.getChannel();
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        // 使用transferForm完成拷贝
        fileChannel02.transferFrom(fileChannel01, 0, fileChannel01.size());

        // 关闭
        fileChannel01.close();
        fileChannel02.close();
        fileInputStream.close();
        fileChannel02.close();
    }
}
