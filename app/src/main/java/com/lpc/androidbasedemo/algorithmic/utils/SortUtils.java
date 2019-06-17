package com.lpc.androidbasedemo.algorithmic.utils;

import android.util.Log;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/25
 * description: 排序算法
 */
public class SortUtils {
    static String TAG = "SortUtils";

    /**
     * 快速排序 升序
     *
     * @param a
     */

    public static void fastSort(int[] a) {
        if(null == a || a.length<=0){
            return;
        }
        fastSort(a,0, a.length-1);
        for (int i = 0; i < a.length; i++) {
//            Log.i(TAG, "fastSort: " + a[i]);
            System.out.println("fastSort: " +a[i]);
        }
    }

    private static void fastSortStart(int[] a,int left,int right){
        int start ,end, temp;
        if(left>right){
            return;
        }
        temp = a[left];
        start = left;
        end = right;
        while (start!=end){

        }
    }

    private static void quicksort(int[] a,int left, int right) {
        int i,j,t,temp;
        if(left>right) {
            return;
        }

        temp=a[left]; //temp中存的就是基准数
        i=left;
        j=right;
        while(i!=j)
                    {
            //顺序很重要，要先从右边开始找
            while(a[j]>=temp && i<j) {
                j--;
            }
            //再找右边的
            while(a[i]<=temp && i<j) {
                i++;
            }
            //交换两个数在数组中的位置
            if(i<j)
            {
                t=a[i];
                a[i]=a[j];
                a[j]=t;
            }
        }
        //最终将基准数归位
        a[left]=a[i];
        a[i]=temp;

        quicksort(a,left,i-1);//继续处理左边的，这里是一个递归的过程
        quicksort(a,i+1,right);//继续处理右边的 ，这里是一个递归的过程
    }

    private static void fastSort(int[] a,int left ,int right){

        if(left<right){
            int temp = a[left];
            int start = left;
            int end = right;
            while (left!=right){
                while (a[right]<= temp&&left<right) {
                    right--;
                }
                while ((a[left]>=temp)&&left<right) {
                    left++ ;
                }
                if(left<right){
                    a[right]=a[right]+a[left];
                    a[left]=a[right]-a[left];
                    a[right]= a[right]-a[left];
                }
            }
            a[start]=a[right];
            a[right]=temp;

            fastSort(a,start,right-1);
            fastSort(a,right+1,end);
        }

    }

    /**
     * 冒泡排序
     * @param a
     */
    public static void maoPaoSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1; j++) {
                if (a[j] > a[j + 1]) {
                    a[j] = a[j] + a[j + 1];
                    a[j + 1] = a[j] - a[j + 1];
                    a[j] = a[j] - a[j + 1];
                }
            }
        }
        for (int i = 0; i < a.length; i++) {
            System.out.print("selectSort: "+a[i]+"\n");
        }

    }

    /**
     * 选择排序
     * a[]={33,21,7,43,2,89,76,100};
     * @param a
     */
    public static void selectSort(int[] a){
        for (int i = 0; i < a.length-1; i++) {
            int k = i;
            for (int j = i+1; j<a.length; j++) {
                if(a[k]>a[j]){
                    k=j;
                }
            }
            if(k!=i){
                a[i]=a[i]+a[k];
                a[k]=a[i]-a[k];
                a[i]=a[i]-a[k];
            }

        }

        for (int i = 0; i < a.length; i++) {
            System.out.print("selectSort: "+a[i]+"\n");
        }
    }

    public static void main(String[] args) {
        int[] a = {2,5,-3,7,5,1};

        String one = "1";
        String two = "1243";
        int max = one.compareTo(two) ;
        System.out.println(max+"---------------");

        fastSort(a);
        fastSort(null);
    }

    public static void newFastTest(int[] nums , int left , int right){
//        if(left<right){
//            int l = left;
//            int r = right;
//            int temp = nums[]
//        }
    }

}
