package com.xxx.NIOTest;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;


/**
 * description: NIO服务器
 * author: xhs 
 * date: 
 * version: 
 **/
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建ServerSockerChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个selector对象
        // 如果要获取socketChannel， 必须通过serverSocketChannel 的accept， 所以服务器要先注册事件
        // 到selector后，才能捕获accept 创建socket
        Selector selector = Selector.open();
        // 绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把serverSocketChannel 注册到 selector 事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while(true){
            // 没有事件发生
            if(selector.select(1000) == 0){
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            // 返回不是0, 就会获取到有事件发生的selectionKey集合
            // 如果返回的大于0， 表示已经获取关注的事件
            // 通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历集合，使用迭代器遍历
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                // 获取到 selectionKey
                SelectionKey next = iterator.next();
                if(next.isAcceptable()){
                    // 有新的客户端连接
                    // 生成一个新的SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端链接成功 生成一个socketchannel" + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    // 将socketChannel注册到selector上, 关注事件为OP_READ， 关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(next.isReadable()){
                    // 如果为可读对象
                    // 通过Key反向获取channel
                    SocketChannel channel = (SocketChannel) next.channel();
                    // 获取到该channel关联的Buffer
                    ByteBuffer attachment = (ByteBuffer) next.attachment();
                    // 将数据放入缓存中
                    channel.read(attachment);
                    System.out.println("客户端发送的数据： " + new String(attachment.array()));
                }
                // 手动从集合中移除当前selectionKey, 防止重复操作
                iterator.remove();
            }
        }
    }
}
