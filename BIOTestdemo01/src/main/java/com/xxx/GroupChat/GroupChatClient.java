package com.xxx.GroupChat;
/**
 * description: 群聊系统客户端， 可以发送信息到指定服务端，接收服务端的消息，基于NIO模式搭建
 * author: xhs
 * date: 2022/4/1
 * version:
 **/
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    // 定义相关属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String usename;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = socketChannel.open(new InetSocketAddress(HOST, 6667));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        usename = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(usename + " 准备好了Ok");
    }

    // 向服务器发送消息
    public void sendInfo(String info){
        info = usename + " 说 " + info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // 读取服务端的消息
    public void readInfo(){
        try{
            int count = selector.select();
            if(count > 0){
                // 有可用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey next = iterator.next();
                    if(next.isReadable()){
                        // 得到相关通道
                        SocketChannel channel = (SocketChannel) next.channel();
                        // 得到一个Buffer
                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        channel.read(allocate);
                        // 把读取到的数据转换成字符串
                        String s = new String(allocate.array());
                        System.out.println(s.trim());
                    }
                }
                iterator.remove();
            }else{
                System.out.println("没有可用的通道");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // 启动客户端
        GroupChatClient ChatClient = new GroupChatClient();
        // 启动一个线程
        new Thread(){
            public void run(){
                while(true){
                    ChatClient.readInfo();
                    try{
                        Thread.sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        // 发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            ChatClient.sendInfo(s);
        }
    }
}
