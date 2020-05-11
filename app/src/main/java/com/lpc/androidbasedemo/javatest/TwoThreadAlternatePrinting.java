package com.lpc.androidbasedemo.javatest;

import java.util.LinkedList;

/**
 * @author : lipengcheng1
 * @date : 2020-05-05
 * desc:
 */
public class TwoThreadAlternatePrinting {
    static Object lock = new Object();
    public static void main(String[] args) {
    int [] ints = {1,2,3,4,5};
    String[] strings = {"a","b","c","d","e"};

    new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (lock){
                    for (int i = 0; i < ints.length; i++) {
                        lock.notify();

                        try {
                            System.out.print(ints[i]);
                            if (i < ints.length-1) {
                                lock.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    for (int i = 0; i < strings.length; i++) {
                        lock.notify();
                        try {
                            System.out.print(strings[i]);
                            if (i < strings.length-1) {
                                lock.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        LinkedList list = new LinkedList();
        list.peek();
        list.poll();
        list.pop();
        list.push(lock);
    }

}
