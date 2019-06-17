package com.lpc.androidbasedemo.test;

import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * @author lipengcheng
 * create at  2019/1/27
 * description:
 */
public class ThreadPoolTest1 {
    private static Executor sExecutor = Executors.newCachedThreadPool();
    private static Executor sExecutor1 = Executors.newFixedThreadPool(5);
    private static Executor sExecutor2 = Executors.newSingleThreadExecutor();
    private static Executor sExecutor3 = Executors.newScheduledThreadPool(5);//计划任务
    private static Executor sExecutor4 = Executors.newWorkStealingPool();
    public static void main(String[] args) {
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                return thread;
            }
        };

        Executor executor = new ThreadPoolExecutor(0,5,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(100),factory);



        for (int i = 0; i <  100; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                        doTask();
                }
            });
        }

    }

    private static void doTask(){
        System.out.print(Thread.currentThread().getName()+"  来了 \n");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(Thread.currentThread().getName()+"  走了 \n");

    }
}
