package com.lpc.androidbasedemo.test;

/*
 * @author lipengcheng
 * create at  2019/1/12
 * description:
 */
public class Person {
    protected static String Name = "张三";

    public static String getName() {
        return Name;
    }

    static {
        System.out.print("Person 静态代码快\n");
    }
    {
        System.out.print("Person 非静态代码块1 \n");
    }

    public Person(){
        System.out.print("Person 构造方法\n");
    }
    {
        System.out.print("Person 非静态代码块2 \n");
    }
}
