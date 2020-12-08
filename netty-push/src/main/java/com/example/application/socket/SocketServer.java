package com.example.application.socket;


import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.application.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>描述: CmpSystemMsgDao</p>
 * <p>公司: 瑞华康源科技有限公司</p>
 * <p>版权: rivamed2018</p>
 *
 * @author wanghualin
 * @version V1.0
 * @date 2019/9/17
 */
public class SocketServer {

    private static Integer members = 0;

    private static List<SocketIOClient> users = new ArrayList<>();
    public void start() {
        Map<String, String> poems = new HashMap<>();

        poems.put("行宫","寥落古行宫，宫花寂寞红。\n" + "白头宫女在，闲坐说玄宗。");
        poems.put("登鹳雀楼","白日依山尽，黄河入海流。\n" + "欲穷千里目，更上一层楼。");
        poems.put("新嫁娘词","三日入厨下，洗手作羹汤。\n" + "未谙姑食性，先遣小姑尝。");
        Configuration config = new Configuration();
        config.setHostname("192.168.111.123");
        config.setPort(8000);
        SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(socketIOClient -> {
            // 将连接的用户保存起来
            users.add(socketIOClient);
        });

        //添加客户端断开连接事件
        server.addDisconnectListener(client -> {
            String sa = client.getRemoteAddress().toString();
            String clientIp = sa.substring(1,sa.indexOf(":"));//获取设备ip
            System.out.println("server:"+clientIp+"："+"客户端已断开连接");
        });
        /*server.addEventListener("poem", Object.class,(SocketIOClient client, Object data, AckRequest ackRequest)->{
            Message message = new Message();
            message.setType((byte) 6);
            message.setChannelName("通道名字");
            message.setDescription("描述");
            message.setChannelId("channelID");
            message.setChannelName("channelName");
            message.setChannelGroupId("greoupId");
            message.setChannelGroupName("greoupName");
            message.setTitle("标题标题标题标题标题标题？？？？");
            message.setText("内容内容内容内容内容内容！！！！！");

            String jmessage = JSON.toJSONString(message);
            System.out.println(jmessage);
            client.sendEvent("hell",jmessage);
            server.getBroadcastOperations().sendEvent("msg",data+" \n"+poems.get(data));
        });*/
        server.start();
    }

    public void pushMsg(){
        Message message = new Message();
        message.setType((byte) 6);
        message.setDescription("描述");
        message.setChannelId("channelID");
        message.setChannelName("channelName");
        message.setChannelGroupId("greoupId");
        message.setChannelGroupName("greoupName");
        message.setTitle("标题标题标题标题标题标题？？？？");
        message.setText("内容内容内容内容内容内容！！！！！");
        System.out.println("目前连接数量"+users.size());
        String jmessage = JSON.toJSONString(message);
        for (SocketIOClient socketIOClient : users){
            socketIOClient.sendEvent("hell",jmessage);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SocketServer socketServer = new SocketServer();
        socketServer.start();
        new Thread(new RunSocket(socketServer)).start();
    }

}

class RunSocket implements Runnable{

    private SocketServer socketServer;
    private RunSocket(){}
    public RunSocket(SocketServer socketServer){
        this.socketServer = socketServer;
    }
    @Override
    public void run() {
        while (true){
            socketServer.pushMsg();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
