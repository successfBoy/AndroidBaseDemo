package com.lpc.androidbasedemo.algorithmic.utils;

import android.util.Log;

import com.lpc.androidbasedemo.common.tool.LogUtils;

import org.xml.sax.helpers.LocatorImpl;

import javax.security.auth.login.LoginException;

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

    public static void fastSort(int a[]) {
        fastSort(a,0, a.length-1);
        for (int i = 0; i < a.length; i++) {
            Log.i(TAG, "fastSort: " + a[i]);
        }
    }

    private static void quicksort(int a[],int left, int right) {
        int i,j,t,temp;
        if(left>right)
            return;

        temp=a[left]; //temp中存的就是基准数
        i=left;
        j=right;
        while(i!=j)
        {
            //顺序很重要，要先从右边开始找
            while(a[j]>=temp && i<j)
                j--;
            //再找右边的
            while(a[i]<=temp && i<j)
                i++;
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

    private static void fastSort(int a[],int left ,int right){
        if(left<right){
            int temp = a[left];
            int start = left;
            int end = right;
            while (left!=right){
                while (a[right]<= temp&&left<right)
                    right--;
                while ((a[left]>=temp)&&left<right)
                    left++ ;
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
    public static void maoPaoSort(int a[]) {
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
            Log.i(TAG, "maoPaoSort: " + a[i]);
        }

    }

    /**
     * a[]={33,21,7,43,2,89,76,100};
     * @param a
     */
    public static void selectSort(int a[]){
        for (int i = 0; i < a.length; i++) {
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
            Log.i(TAG, "selectSort: "+a[i]);
        }
    }

}
