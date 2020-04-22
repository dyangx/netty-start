package com.netty.server.chapter01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class NettyServer {

    private final int port;

    public NettyServer(int port){
        this.port = port;
    }
    // mvn exec:java -Dexec.args="9000"
    public static void main(String[] args) throws InterruptedException {
        if(args.length != 1){
            System.err.println("端口参数错误>>>>>>>>>>>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new NettyServer(port).startServer();
    }
    public void startServer() throws InterruptedException {
        System.out.println("启动服务端。。。。。。。。");
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    // 指定nio 传输的channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的套接字地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加echoServerHandler 到channel管道
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(echoServerHandler);
                        }
                    });
            // 异步绑定服务器，调用sync() 直到绑定完成
            ChannelFuture f = bootstrap.bind().sync();
            //获取channel 的closeFuture 并且阻塞当前线程直到完成
            f.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup 释放资源
            group.shutdownGracefully().sync();
        }
    }
}
