package com.example.application.server;

import com.example.application.message.Message;
import com.example.application.message.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description: 握手验证
 * @date: Created in 2019/9/18 13:42
 */
public class ConnectHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
//        ChannelInboundHandlerAdapter
        Message message = (Message) msg;
        if(message != null && message.getType() == MessageType.CONNECT_REQ.getValue()){
            Message response = buildMessage(MessageType.CONNECT_SUCCESS.getValue());
            ctx.writeAndFlush(response);
        }else{
            ctx.fireChannelRead(message);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message){
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private Message buildMessage(byte result){
        Message msg = new Message();
        msg.setType(result);
        return msg;
    }
}

