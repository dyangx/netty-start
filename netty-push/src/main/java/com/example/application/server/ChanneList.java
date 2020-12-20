package com.example.application.server;


import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yangjie
 * @date: Created in 2019/9/18 13:55
 */
public class ChanneList {

    private volatile static List<Channel> channelList = null;

    private ChanneList(){}

    public static List<Channel> getInstance(){
        if(channelList == null){
            synchronized (ChanneList.class){
                if(channelList == null){
                    channelList = new ArrayList<>();
                }
            }
        }
        return channelList;
    }
}
