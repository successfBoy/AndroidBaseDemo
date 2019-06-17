package com.lpc.androidbasedemo.test;

import java.util.concurrent.Semaphore;

/*
 * @author lipengcheng
 * create at  2019/1/27
 * description:
 */
public class ThreadPoolTest {
    private static Semaphore sSemaphore = new Semaphore(5);
    public static void main(String[] args) {
        for (int i = 0; i <  100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        doTask();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static void doTask() throws InterruptedException {
        sSemaphore.acquire();
        System.out.print(Thread.currentThread().getName()+"  来了 \n");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(Thread.currentThread().getName()+"  走了 \n");

        sSemaphore.release();
    }
}
