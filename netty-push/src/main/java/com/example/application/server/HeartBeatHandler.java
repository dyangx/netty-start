package com.example.application.server;

import com.example.application.message.Message;
import com.example.application.message.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:  心跳检测
 * @author: yangjie
 * @date: Created in 2019/9/18 13:45
 */
public class HeartBeatHandler extends SimpleChannelInboundHandler<Message> {


    //加入到在线列表，只有在线用户才可以实时推送
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ChanneList.getInstance().add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message){
        //如果是心跳包ping，则返回pong
        if(message != null && message.getType() == MessageType.HEARTBEAT_REQ.getValue()){
            Message response = buildMessage(MessageType.HEARTBEAT_RESP.getValue());
            ctx.writeAndFlush(response);
        }else{
            ctx.fireChannelRead(message);
        }
    }

    private Message buildMessage(byte result){
        Message msg = new Message();
        msg.setType(result);
        return msg;
    }
}

