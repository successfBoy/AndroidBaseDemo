package com.lpc.androidbasedemo.algorithmic.utils;

/*
 * @author lipengcheng
 * @emil lipengcheng1@jd.com
 * create at  2018/9/28
 * description:
 */
public class CommonAlgorithmic {
    static final String TAG = "CommonAlgorithmic";

    public static void main(String[] args) {
        reverseNode();
    }

    public static Node reverseNode() {
        Node node = new Node(0);
        Node current = node;
        for (int i = 1; i < 10; i++) {
            current.next = new Node(i);
            current = current.next;
        }
        node = reverseNodeStart(node);
        printNode(node);
        return node;
    }

    public static Node reverseNodeStart(Node node){
        Node pre = null;
        Node current = node;
        while (current!= null){
            Node next = current.next;
            current.next = pre;

            pre = current;
            current = next;
        }
        return pre;

    }

    public static void printNode(Node node) {
        Node current = node;
        while (current != null) {
            System.out.print("reverseNode: " + current.val + "\n");
            current = current.next;
        }
    }

    static class Node {
        public Node(int val) {
            this.val = val;
        }

        public int val;
        Node next;
    }

    /*
     0->1->2->3->4

     4->3->2->1->0

     */
    public static Node reverse(Node node) {
        Node prev = null;
        Node now = node;
        while (now != null) {
            Node next = now.next;
            now.next = prev;
            prev = now;
            now = next;
        }

        return prev;
    }

    /**
     * 链表反转
     * @param node
     * @return
     */
    public static Node reverseNode(Node node) {
        Node head = null;
        Node current = node;
        while (current!= null) {
            Node next = current.next;
            current.next = head;

            head = current;
            current = next;

        }
        return head;
    }


}
