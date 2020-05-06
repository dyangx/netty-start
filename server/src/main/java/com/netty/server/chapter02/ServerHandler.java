package com.netty.server.chapter02;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: yangjie
 * @date: Created in 2020/4/27 9:20
 */
public class ServerHandler {
    
    private ExecutorService executorService;

    public ServerHandler(int maxPoolSize,int queueSize){
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxPoolSize,120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void excute(Runnable runnable){
        executorService.execute(runnable);
    }
}
