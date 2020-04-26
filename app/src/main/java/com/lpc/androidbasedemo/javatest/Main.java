package com.lpc.androidbasedemo.javatest;

import org.openjdk.jol.info.ClassLayout;

/*
 * @author lipengcheng
 * create at  2019/1/6
 * description:
 */
public class Main {

    public static void main(String[] args) {
        Student student = new Student("张三");

        /*
        com.lpc.androidbasedemo.javatest.Student object internals:
         OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
              0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
              4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
              8     4        (object header)                           7d c1 00 f8 (01111101 11000001 00000000 11111000) (-134168195)
             12     4    int Person.age                                13
        Instance size: 16 bytes
        Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
         */

        student.hashCode();

        System.out.println(ClassLayout.parseInstance(student).toPrintable());

        /**
         * com.lpc.androidbasedemo.javatest.Student object internals:
         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
         *       0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
         *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
         *       8     4        (object header)                           7d c1 00 f8 (01111101 11000001 00000000 11111000) (-134168195)
         *      12     4    int Person.age                                13
         * Instance size: 16 bytes
         * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
         */
        System.out.println(ClassLayout.parseInstance(student).toPrintable());

        synchronized (student){
            /**
             * com.lpc.androidbasedemo.javatest.Student object internals:
             *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
             *       0     4        (object header)                           30 19 b6 0c (00110000 00011001 10110110 00001100) (213260592)
             *       4     4        (object header)                           00 70 00 00 (00000000 01110000 00000000 00000000) (28672)
             *       8     4        (object header)                           7d c1 00 f8 (01111101 11000001 00000000 11111000) (-134168195)
             *      12     4    int Person.age                                13
             * Instance size: 16 bytes
             * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
             */
            System.out.println(ClassLayout.parseInstance(student).toPrintable());


            /**
             * jvm ---- 概念/标准
             * openjdk项目（代码） ---- hotspot 源码C++
             * java.exe ----- openjdk 编译之后
             */

            //================================================
            /**
             * 对象头：
             * 每个GC管理的堆对象开头的公共结构。（每个 oop 都指向一个对象头。）包括堆对象的布局，类型，GC状态
             * ，同步状态和标识哈希码的基本信息。有两个词（字长）组成，在数组中，后面紧跟着一个长度字段。
             * 注意：java对象和vm内部对象都有一个通用的对象头格式。
             */

            /**
             * 大小端
             */

            /**
             * Sync extends AbstractQueuedSynchronizer
             */
        }
    }
}
