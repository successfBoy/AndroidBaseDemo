package com.lpc.androidbasedemo.test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

/*
 * @author lipengcheng
 * create at  2018/12/30
 * description:
 */
public class StaticC {
//    public synchronized int ss = 0;
    public static void main(String[] args) {
        String s =StaticB.str;
        Integer.parseInt("000");
        Integer.valueOf("000");
        Integer.toString(10101);
//        Collection.synchronizeMap();
        HashSet set =  new HashSet();
        set.add("dd");
//        set.forEach();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
            System.out.print(next);
        }

        TreeSet treeSet =  new TreeSet();
        treeSet.add("1");

        TreeMap<String,String> treeMap = new TreeMap<>();
        treeMap.put("first","first");
        treeMap.get("");

        LinkedList<String> linkedList =  new LinkedList<>();
        linkedList.add("e");
        linkedList.get(0);

        Collections.synchronizedList(linkedList);
        Collections.synchronizedMap(treeMap);


        Thread thread = new Thread();
        thread.run();
        Semaphore semaphore = new Semaphore(5);
        try {
            semaphore.acquire();
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//
//        HashMap<>


    }
}
