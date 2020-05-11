package com.lpc.androidbasedemo.jianzhioffer;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author : lipengcheng1
 * @date : 2020-04-27
 * desc:
 *
 * 把数组排成最小的数
 *
 * 题目描述：
 *
 *   输入一个正整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。
 * 例如输入数组{3，32，321}，则打印出这三个数字能排成的最小数字为321323。
 *
 *   解题思路：
 *
 *   本题最直观的解法就是求出数组中所有数字的全排列，然后比较所有的排列，最后找到最小的排列，但是时间复杂度为O(n!)，所以不是一个好的解法。
 *
 *   换一种思路可以发现，本题实际上希望我们找到一个排序规则，数组根据这个排序规则进行重排之后可以连成一个最小的数字。要确定这样的排序规则，也就是对于两个数字m和n，通过一个规则确定哪个应排在前面。
 *
 *   根据题目要求，我们可以发现，两个数字m和n能拼接成mn和nm，如果mn<nm，那m应该在前；如果nm<mn，那么n应该在前。因此，我们得到的排序规则如下：
 *
 * 若mn>nm，则m大于n
 * 若mn<nm，则m小于n
 * 若mn=nm，则m等于n
 *   根据上述规则，我们需要先把数字转换成字符串再进行比较，因为需要拼接起来。比较完之后按顺序连接成一个字符串即可。
 */
public class _32_PrintMinNumber {

    static int[] array = {3,32,321};
    public static void main(String[] args) {
        String number = PrintMinNumber(array);
        System.out.println(number);
    }


   static class compareStr implements Comparator<String> {
        @Override
        public int compare(String m,String n) {
            String mn=m+n,nm=n+m;
            return mn.compareTo(nm);  //mn小于nm返回-1，等于返回0，大于返回1
        }
    }

    public static String PrintMinNumber(int [] numbers) {
        String result="";
        if(numbers==null||numbers.length<=0) {
            return result;
        }
        int len=numbers.length;
        String[] str=new String[len];
        for(int i=0;i<len;i++) {
            str[i]=String.valueOf(numbers[i]);
        }
        compareStr c=new compareStr();
        Arrays.sort(str,c);

        for(String s:str) {
            result += s;
        }
        return result;
    }
}
