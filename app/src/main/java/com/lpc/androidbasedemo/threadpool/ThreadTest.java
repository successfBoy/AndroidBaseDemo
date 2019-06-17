package com.lpc.androidbasedemo.threadpool;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;

/*
 * @author lipengcheng
 * create at  2018/12/30
 * description:
 */
public class ThreadTest {
    private final static String TAG = "ThreadTest";

    public static void main(String[] args) {


        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 100; j++) {
                    System.out.print("------>" + 1 + "\n");
                }

            }
        });
        final Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 100; j++) {
                    System.out.print("------>" + 2 + "\n");
                }

            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 100; j++) {
                    System.out.print("------>" + 3 + "\n");
                }

            }
        });


        thread3.start();
        thread1.start();
        thread2.start();

        System.out.print("住县城");


    }

    public static int[] twoSum(int[] nums, int target) {
        int[] sou = {-1, -1};
        for (int i = 0; i < nums.length - 1; i++) {
            int s = target - nums[i];
            int inex = (nums.length - i) / 2;
            if (s > nums[inex]) {

            } else {
                for (int m = i + 1; m < inex; m++) {

                }
            }

        }
        System.out.print(sou[0] + " " + sou[1]);
        return sou;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int[] twoSum2(int[] nums, int target) {
        int[] temp = new int[2];
        ArrayMap<Integer, Integer> hs = new ArrayMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hs.containsKey(target - nums[i])) {
                temp[1] = i;   //之前存进去的值肯定是index较小的，所以放在第一位
                temp[0] = hs.get(target - nums[i]);
                return temp;
            }
            hs.put(nums[i], i);
        }
        return temp;
    }


}
