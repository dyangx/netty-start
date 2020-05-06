package com.netty.server.chapter02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author: yangjie
 * @date: Created in 2020/4/27 13:56
 */
public class TimeServer implements Runnable {

    private Socket socket;

    public TimeServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            System.err.println("发送：");
            out.println("来自福王府段的消息-》》》");
            System.err.println("接收：");
            while (true){
                String s = in.readLine();
                if(s != null){
                    System.out.println(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                out.close();
                out = null;
            }
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
