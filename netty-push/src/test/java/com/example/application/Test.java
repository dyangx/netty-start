package com.example.application;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yangjie
 * @date: Created in 2019/9/23 14:20
 */
public class Test {

    volatile static List<String> list = new ArrayList<>(10240);

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(()->{
            for(int i=0;i<3000;i++){
                list.add(i+"");
            }
        });
        Thread t2 = new Thread(()->{
            for(int i=0;i<3000;i++){
                list.add(i+"");
            }
        });
        Thread t3 = new Thread(()->{
            for(int i=0;i<3000;i++){
                list.add(i+"");
            }
        });
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(2000);
        System.out.println(list.size());
    }
}
