package com.lpc.androidbasedemo.algorithmic.utils;

import android.util.Log;
import android.widget.Filter;
import android.widget.ListView;

/*
 * @author lipengcheng
 * @emil lipengcheng1@jd.com
 * create at  2018/9/28
 * description:
 */
public class CommonAlgorithmic {
    static final String TAG = "CommonAlgorithmic";

    public static ListNode reverseListNode() {
        ListNode node = new ListNode(0);
        ListNode current = node;

        for (int i = 1; i < 10; i++) {
            current.next = new ListNode(i);
            current = current.next;
        }

        reverse(node);

//        printNode(node);
        return node;
    }

    public static void reverse(ListNode node){
        ListNode head = node;
        ListNode last = node;
        //o-->o -->o
        //o<--o -->o
        //o<--o <--o
        ListNode first = node;
        ListNode second = first.next;


        while(first != null){


            if(second == null){
                head = first;
                last.next = null;
                break;
            }

            ListNode third = second.next;//找到第三个

            second.next = first;//第二个翻转

            if(third == null){
                head = second;
                last.next = null;
                break;
            }

            if(third.next == null){
                break;
            }
            third.next.next = second;//第三个翻转

            first = third;
            second = third.next;
        }

        printNode(head);


    }

    public static void printNode(ListNode node) {
        ListNode current = node;
        while (current != null) {
            Log.i(TAG, "reverseListNode: " + current.val);
            current = current.next;
        }
    }

    static class ListNode {
        public ListNode(int val) {
            this.val = val;
        }

        public int val;
        ListNode next;
    }
}
