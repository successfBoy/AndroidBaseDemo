package com.lpc.androidbasedemo.test;

/*
 * @author lipengcheng
 * create at  2019/1/12
 * description:
 */
public class Student extends Person {
//    public static String Name = "李四";
    public static String newName = "李四";
    int age;
    String name;
    public Student(String name,int age){
        this.age = age;
        this.name = name;
    }
    public static String getName(){
        return newName;
    }
    {
        System.out.print("student 非静态代码块2 \n");
    }

    public Student(){
        System.out.print("student 构造方法\n");
    }

    static {
        System.out.print("Sudent 静态代码快\n");
    }


    {
        System.out.print("student 非静态代码块1 \n");
    }

    @Override
    public String toString() {
        return "[ "+name + " , "+age+" ]";
    }
}
