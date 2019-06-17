package com.lpc.androidbasedemo.threadpool;

import android.support.annotation.NonNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * @author lipengcheng
 * @emil lipengcheng1@jd.com
 * create at  2018/9/23
 * description:
 */
public class ThreadPoolFactory {
    /**
     *  corePoolSize：核心池的大小，这个参数跟后面讲述的线程池的实现原理有非常大的关系。在创建了线程池后，默认情况下，线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，除非调用了prestartAllCoreThreads()或者prestartCoreThread()方法，从这2个方法的名字就可以看出，是预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程。默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
        maximumPoolSize：线程池最大线程数，这个参数也是一个非常重要的参数，它表示在线程池中最多能创建多少个线程；
        keepAliveTime：表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0；
        unit：参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性：
     */
    public void createThreadPool(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,3000,
                TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(5));
        ThreadPoolExecutor executor1;

        executor1 = new ThreadPoolExecutor(5,10,3000,TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(),new MyThreadFactory());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };

        executor.execute(runnable);

        executor.shutdown();
        Integer i = 0;

    }

    class MyThreadFactory implements ThreadFactory{

        /**
         * Constructs a new {@code Thread}.  Implementations may also initialize
         * priority, name, daemon status, {@code ThreadGroup}, etc.
         *
         * @param r a runnable to be executed by new thread instance
         * @return constructed thread, or {@code null} if the request to
         * create a thread is rejected
         */
        @Override
        public Thread newThread(@NonNull Runnable r) {
            return null;
        }
    }
}
