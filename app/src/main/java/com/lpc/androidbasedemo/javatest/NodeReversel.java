package com.lpc.androidbasedemo.javatest;

/**
 * @author : lipengcheng1
 * @date : 2020-05-07
 * desc:
 */
public class NodeReversel {

    public static void main(String[] args) {

        Node next = null;
        Node current = null;
        for (int i = 100; i > 0; i--) {
            if (next == null) {
                next = new Node(100);
            } else {
                current = new Node(i);
                current.next = next;
                next = current;
            }

        }

        Node node = next;
        while (node != null) {
            System.out.println(node.head);
            node = node.next;
        }

        Node node2 = again(next);
        System.out.println("=====================");
        while (node2 != null) {
            System.out.println(node2.head);
            node2 = node2.next;
        }
    }

    private static Node resveral(Node node) {
        if (node == null || node.next == null) {
            return node;
        }

        Node pre = null;
        Node current = node;

        while (current != null) {
            Node next = current.next;
            current.next = pre;
            pre = current;

            current = next;
        }

        return pre;
    }








































    private static Node again(Node node){
        if (node == null || node.next == null){
            return node;
        }

        Node pre = null;
        Node current = node;
        while (current != null){
            Node next = current.next;
            current.next = pre;
            pre = current;
            current = next;
        }

        return pre;
    }

    static class Node {

        private Object head;//数据域
        private Node next;//指针域

        public Node(Object data) {
            this.head = data;
        }
    }
}
