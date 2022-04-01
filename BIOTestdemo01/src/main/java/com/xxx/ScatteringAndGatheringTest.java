package com.xxx;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * description: Netty 的Buffer的分散与聚合函数
 * author: xhs
 * date:
 * version:
 **/




public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {
        /**
         * Scattering : 将数据写入到Buffer时，可以采用Buffer数组依次写入
         * Gathering ： 从Buffer读取数据时， 可以采用buffer数组依次读入
         **/

        // 使用 ServerSocketChannel 和 SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到socket， 并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;
        while(true){
            int byteRead = 0;
            while(byteRead < messageLength){
                long read = socketChannel.read(byteBuffers);
                byteRead++;
                System.out.println("byteRead = " + byteRead);
                // 使用流打印
                Arrays.asList(byteBuffers).stream().map(buffer -> "position=" +
                        buffer.position() + ", limit=" + buffer.limit()).forEach(System.out::println);
            }
            // 将所有的buffer进行flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            // 将数据读出显示到客户端
            long bytewrite = 0;
            while(bytewrite < messageLength){
                long l = socketChannel.write(byteBuffers);
                bytewrite += l;
            }

            // 将所有buffer进行clear
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
            System.out.println("byteRead:=" + byteRead + "bytewrite=" + bytewrite + "messagelength" + messageLength);
        }
    }
}
