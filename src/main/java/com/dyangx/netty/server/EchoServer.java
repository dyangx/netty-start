package com.dyangx.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;

public class EchoServer {

    private static final int port = 7070;

    public static void main(String[] args) {

        new Thread(() ->{
            final EchoServerHandler echoServerHandler = new EchoServerHandler();
            EventLoopGroup group = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            try {
                serverBootstrap.group(group)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(new InetSocketAddress(port))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(echoServerHandler);
                            }
                        });
                ChannelFuture f = serverBootstrap.bind().sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
