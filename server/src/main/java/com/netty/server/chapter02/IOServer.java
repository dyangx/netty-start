package com.netty.server.chapter02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: yangjie
 * @date: Created in 2020/4/27 9:20
 */
public class IOServer {

    public static void main(String[] args) throws IOException {
        new IOServer().start(7070);
    }


    public void start(int port) throws IOException {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
            Socket socket = null;
            ServerHandler serverHandler = new ServerHandler(50,10000);
            while (true){
                socket = serverSocket.accept();
                serverHandler.excute(new TimeServer(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }

}
