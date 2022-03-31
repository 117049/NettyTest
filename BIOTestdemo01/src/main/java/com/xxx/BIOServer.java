package com.xxx;
/**
 * description: Netty 搭建BIO模型
 * author: xhs 
 * date: 2022/3/31
 * version: 
 **/
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        // 创建线程池进行通信
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("启动服务器");


        // 监听
        while(true){
            System.out.println("线程Id:" + Thread.currentThread().getId() + "线程名字:" + Thread.currentThread().getName());
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    //我们重写
                    //可以和客户端通讯;
                    handler(socket);
                }
            });
        }
    }
    public static void handler(Socket socket) {
        try {
            System.out.println("handler" + "线程Id:" + Thread.currentThread().getId() + "线程名字:" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端的消息
            while(true){
                System.out.println("read....");
                int read = inputStream.read(bytes);
                if(read != -1){
                    //循环显示信息
                    System.out.println(new String(bytes, 0, read));
                }else{
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                System.out.println("关闭链接");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
