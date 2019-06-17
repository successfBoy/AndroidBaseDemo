package com.lpc.androidbasedemo.test;

import java.util.ArrayList;
import java.util.List;

/*
 * @author lipengcheng
 * create at  2019/1/12
 * description:
 */
public class PersonTest {
    public static void main(String[] args) {
        System.out.print(Student.Name+"<=======Name========\n");
        System.out.print(Student.getName()+"<====getName====\n");
        Student student =  new Student();
        Student student1 =  new Student();
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("a");
        list.add("a");
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals("b")){
                list.remove(i);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i)+"\n");
        }
    }
}
