package com.example.application.server;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yangjie
 * @date: Created in 2019/9/23 10:53
 */
public class ChannelTest<T> {

    private static List<Channel> channelList = new ArrayList<>();


    public ChannelTest<T> get(){
        return this;
    }

}
