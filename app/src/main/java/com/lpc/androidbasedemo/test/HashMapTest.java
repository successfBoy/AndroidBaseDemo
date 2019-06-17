package com.lpc.androidbasedemo.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * @author lipengcheng
 * create at  2019/1/27
 * description: HashMap 相关
 */
public class HashMapTest {
    public static void main(String[] args) {
        HashMap<Integer,Student> hashMap = new HashMap<>();
        Student student = new Student("张三",25);
        hashMap.put(1,student);
        Student student1 = new Student("李四",21);
        hashMap.put(2,student1);
        Student student2 = new Student("王五",22);
        hashMap.put(3,student2);
        Student student3 = new Student("孙起",18);
        hashMap.put(4,student3);

        System.out.print(hashMap+"\n");

        HashMap<Integer,Student> sortHashMap = sortHashMap(hashMap);
        System.out.print(sortHashMap+"\n");
    }

    private static HashMap<Integer, Student> sortHashMap(HashMap<Integer, Student> hashMap) {

        Set<Map.Entry<Integer, Student>> entries = hashMap.entrySet();

        ArrayList<Map.Entry<Integer, Student>> entries1 = new ArrayList<>(entries);
        Collections.sort(entries1, new Comparator<Map.Entry<Integer, Student>>() {
            @Override
            public int compare(Map.Entry<Integer, Student> o1, Map.Entry<Integer, Student> o2) {
//                o1.getValue().name.compareTo()
                return o1.getValue().age-o2.getValue().age;
            }
        });
        LinkedHashMap<Integer,Student> newHashMap = new LinkedHashMap<>();

        for (Map.Entry<Integer, Student> e : entries1) {
            newHashMap.put(e.getKey(),e.getValue());
        }
        return newHashMap;
    }

    /**
     * java 引用类型
     * 强引用 弱引用 虚引用 软引用
     */
}
