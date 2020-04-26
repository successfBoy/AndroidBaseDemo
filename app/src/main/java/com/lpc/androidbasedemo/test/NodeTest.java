package com.lpc.androidbasedemo.test;


import java.io.IOError;
import java.io.IOException;

/*
 * @author lipengcheng
 * create at  2019/1/7
 * description:
 */
public class NodeTest {
    static class Node{
        public Node(int val){
            this.val = val;
        }
        int val;
        Node next;
    }

    public static void main(String[] args) {
//        int count = 10;
//        Node node = null;
//        Node next;
//        while (count >0){
//            if(null == node){
//                node = new Node(count);
//            }
//            count --;
//            next =new Node(count);
//            next.next=node;
//            node = next;
//        }
//        Linklist linklist = new Linklist();
//        linklist.add(new Node(2));
//        System.out.print(node);
//        new NullPointerException();
//        new IndexOutOfBoundsException();
//            exceptionTest();


        int[] a = testFebona(20);

        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]);
            System.out.print("\n");
        }


    }

    /**
     * 斐波那契数列
     * @param n
     * @return
     */
    private static int[] testFebona(int n){
        int []f = new int[n];
        int a=0,b=1,c;
        for (int i = 0; i < n; i++) {
            if(i == 0){
                f[i]=a;
            }else if (i == 1){
                f[i]=b;
            }else {
                c = a+b;
                f[i]=c;
                a=b;
                b=c;
            }
        }
        return f;
    }


    static class Linklist{
        public void LinkedList(){
            length = 0;
            mNode = new Node(0);
        }
        Node mNode;
        int length;
        public void add(Node node){
            node.next = mNode;
            mNode = node;
        };
    }

//    public static void exceptionTest() throws IllegalAccessException {
//        throw new IllegalAccessException();
//    }
//    public static void exceptionTest() throws IOException, IllegalAccessException {
//        if(Log.DEBUG==3){
//            throw new IOException();
//        }else if(Log.DEBUG==2){
//            throw new IllegalAccessException();
//        }else if(Log.DEBUG==1){
//            throw new RuntimeException();
//        }else if(Log.DEBUG==0){
//            throw new IOError(new Throwable());
//        }
//    }
    public static void exceptionTest() {

        throw new IOError(new IOException());
    }
}
