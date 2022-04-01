package com.xxx.GroupChat;
/**
 * description: 群聊系统服务端，可以监控客户端上线发送消息，转发消息到客户端， 基于NIO模式搭建
 * author: xhs 
 * date: 2022/4/1
 * version: 
 **/
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    // 定义属性
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT = 6667;

    // 构造器
    public GroupChatServer(){
        try{
            // 得到选择器
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // 监听
    public void listen(){
        // 循环处理
        while(true){
            try {
                int select = selector.select(2000);
                if(select > 0){
                    // 有事件需要处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        // 取出selectionkey
                        SelectionKey next = iterator.next();
                        // 监听到accept
                        if(next.isAcceptable()){
                            SocketChannel sc = serverSocketChannel.accept();
                            sc.configureBlocking(false);
                            // 将该sc注册到selector
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + " 上线 ");

                        }
                        if(next.isReadable()){
                            // 通道发送read信息，即通道可读状态
                            // 处理读的方法
                            readData(next);
                        }
                        iterator.remove();
                    }

                }else{
                    System.out.println("等待。。");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // 读取客户端消息
    private void readData(SelectionKey key){
        SocketChannel channel = null;
        try{
            // 取到关联的channel
            channel = (SocketChannel) key.channel();
            // 创建Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);
            // 根据count值做处理
            if(count > 0){
                // 把缓冲区的数据转换为字符串
                String s = new String(buffer.array());
                System.out.println("客户端发送的消息：" + s);
                // 向其他客户端转发消息
                sendInfoToOtherClients(s, channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress() + " 离线");
                // 取消注册
                key.cancel();
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    // 转发消息给其他客户端
    private void sendInfoToOtherClients(String ms, SocketChannel self) throws IOException {
        System.out.println("消息转发中");
        // 遍历找到所有注册到selector的SocketChannel，并排除self
        for(SelectionKey key : selector.keys()){
            // 通过key获取对应的channel
            Channel channel = key.channel();
            // 排除自己
            if(channel instanceof SocketChannel && channel != self){
                // 转型
                SocketChannel dest = (SocketChannel) channel;
                // 将ms 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(ms.getBytes());
                // 将buffer 的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        // 创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
