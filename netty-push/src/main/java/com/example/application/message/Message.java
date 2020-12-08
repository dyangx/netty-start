package com.example.application.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: yangjie
 * @date: Created in 2019/9/18 13:31
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte type;

    /*************通道设置***********/

    private String name;
    private String description;
    private String channelId;
    private String channelName;
    private String channelGroupId;
    private String channelGroupName;

    /*******通知内容*********/
    private String title;
    private String text;
}
