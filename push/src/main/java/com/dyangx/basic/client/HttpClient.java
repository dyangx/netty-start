package com.dyangx.basic.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * EventLoopGroup 是 Netty 的核心处理引擎，
 * 其实 EventLoopGroup 是 Netty Reactor 线程模型的具体实现方式，Netty 通过创建不同的 EventLoopGroup 参数配置，
 * 就可以支持 Reactor 的三种线程模型：
 *
 * 1.单线程模型：EventLoopGroup 只包含一个 EventLoop，Boss 和 Worker 使用同一个EventLoopGroup；
 *
 * EventLoopGroup group = new NioEventLoopGroup(1);
 * ServerBootstrap b = new ServerBootstrap();
 * b.group(group)
 *
 * 2.多线程模型：EventLoopGroup 包含多个 EventLoop，Boss 和 Worker 使用同一个EventLoopGroup；
 *
 *EventLoopGroup group = new NioEventLoopGroup();
 * ServerBootstrap b = new ServerBootstrap();
 * b.group(group)
 *
 * 3.主从多线程模型：EventLoopGroup 包含多个 EventLoop，Boss 是主 Reactor，Worker 是从 Reactor，
 * 它们分别使用不同的 EventLoopGroup，主 Reactor 负责新的网络连接 Channel 创建，然后把 Channel 注册到从 Reactor。
 *
 * EventLoopGroup bossGroup = new NioEventLoopGroup();
 * EventLoopGroup workerGroup = new NioEventLoopGroup();
 * ServerBootstrap b = new ServerBootstrap();
 * b.group(bossGroup, workerGroup)
 *
 */
public class HttpClient {

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.connect("127.0.0.1",8088);
    }

    public void connect(String host,int port) throws Exception {
        // 事件调度层，本质是一个线程池，负责接收I/O请求，并且分配线程处理请求
        // 1. 一个 EventLoopGroup 往往包含一个或者多个 EventLoop。
        //    EventLoop 用于处理 Channel 生命周期内的所有 I/O 事件，如 accept、connect、read、write 等 I/O 事件
        // 2.EventLoop 同一时间会与一个线程绑定，每个 EventLoop 负责处理多个 Channel
        // 3.每新建一个 Channel，EventLoopGroup 会选择一个 EventLoop 与其绑定。
        //    该 Channel 在生命周期内都可以对 EventLoop 进行多次绑定和解绑
        EventLoopGroup group = new NioEventLoopGroup();
        // 引导器，负责程序的启动、初始化、服务器连接
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            // 设置channel类型，eg：OioServerSocketChannel、EpollServerSocketChannel
            b.channel(NioSocketChannel.class);
            // 设置 Channel 参数 ， ServerBootstrap 设置 Channel 属性有option和childOption两个方法，
            // option 主要负责设置 Boss 线程组，而 childOption 对应的是 Worker 线程组
            b.option(ChannelOption.SO_KEEPALIVE,true);
            // 注册 ChannelHandler
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new HttpResponseDecoder());
                    socketChannel.pipeline().addLast(new HttpRequestEncoder());
                    socketChannel.pipeline().addLast(new HttpClientHandler());
                }
            });
            ChannelFuture future = b.connect(host,port).sync();
            URI uri = new URI("http://127.0.0.1:8088");
            String content = "hello world !";
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(content.getBytes(StandardCharsets.UTF_8)));
            request.headers().set("host", host);
            request.headers().set("connection", "keep-alive");
            request.headers().set("content-length", request.content().readableBytes());
            future.channel().write(request);
            future.channel().flush();
//            future.channel().closeFuture().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
