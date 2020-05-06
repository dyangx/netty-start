package com.netty.client.chapter02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author: yangjie
 * @date: Created in 2020/4/27 9:08
 */
public class IOClient {

    public static void main(String[] args) {
        new IOClient().start(7070,"127.0.0.1");
    }

    public void start(int port,String ip){
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(ip,port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println("来自客户端的消息-》》》----------");
            System.err.println("接收：");
            System.out.println(in.readLine());
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
