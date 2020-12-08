package com.example.application.server;

import com.example.application.message.Message;
import com.example.application.message.MessageType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.List;

/**
 * @description:  服务端启动
 * @author: yangjie
 * @date: Created in 2019/9/18 13:53
 */
public class PushServer {

    public void bind() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bs = new ServerBootstrap();
        bs.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast(new ObjectEncoder());
                        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                        //心跳超时
                        p.addLast(new ReadTimeoutHandler(-1));
                        p.addLast(new ConnectHandler());
                        p.addLast(new HeartBeatHandler());
                    }
                });
        bs.bind(8000).sync();
        System.out.println("com.liu.nettypushtest.server 8000 start....");
    }

    //消息推送
    public void push(){
        List<Channel> channels = ChanneList.getInstance();
        System.out.println("push 消息 + " + channels.size());
        Message message = new Message();
        message.setType(MessageType.MSG_PUSH.getValue());
        message.setChannelName("通道名字");
        message.setDescription("描述");
        message.setChannelId("channelID");
        message.setChannelName("channelName");
        message.setChannelGroupId("greoupId");
        message.setChannelGroupName("greoupName");
        message.setTitle("标题标题标题标题标题标题？？？？");
        message.setText("内容内容内容内容内容内容！！！！！");
        for (Channel channel : channels){
            if(channel.isActive()){
                channel.writeAndFlush(message);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        PushServer pushServer = new PushServer();
        pushServer.bind();
        new Thread(new Go(pushServer)).start();
    }
}

class Go implements Runnable{
    private PushServer pushServer;
    private Go(){}
    public Go(PushServer pushServer){
        this.pushServer = pushServer;
    }
    @Override
    public void run() {
        while (true){
            pushServer.push();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

