package com.lpc.androidbasedemo.jianzhioffer;

/**
 * @author : lipengcheng1
 * @date : 2020-04-26
 * desc:
 *
 * 数组中出现次数超过一半的数字
 *
 *   题目描述：
 *
 *   HZ偶尔会拿些专业问题来忽悠那些非计算机专业的同学。今天测试组开完会后,他又发话了:在古老的一维模式识别中,
 * 常常需要计算连续子向量的最大和,当向量全为正数的时候,问题很好解决。但是,如果向量中包含负数,是否应该包含某个负数,
 * 并期望旁边的正数会弥补它呢？例如:{6,-3,-2,7,-15,1,2,2},连续子向量的最大和为8(从第0个开始,到第3个为止)。
 * 给一个数组，返回它的最大连续子序列的和，你会不会被他忽悠住？(子向量的长度至少是1)。
 *
 *   解题思路：
 *
 *   本题可以看做是一个多阶段决策找最优解的问题，因此可以用典型的动态规划思想来求解。
 * 用 res[ i ] 表示以第 i 个元素结尾的子数组的最大和，那么有以下递推公式：
 * res[ i ]=max(res[ i-1]+data[ i ],data[ i ]).
 *
 *   这个公式的含义是：当以第i-1个数字结尾的子数组中所有数字的和小于0时，把这个负数与第i个数累加，
 * 则得到的和比第i个数字本身还要小，所以这种情况下res[ i ]就是第i个数字本身。
 * 反之，如果以第i-1个数字结尾的子数组中所有数字的和大于0，
 * 则与第i个数字累加就得到以第i个数字结尾的子数组中所有数字的和。
 */
public class _30_FindGreatestSumOfSubArray {

    static int[] array = {6,-3,-2,7,-15,1,2,2};

    public static void main(String[] args) {
        int sumOfSubArray = selfResolution(array);
        System.out.println(sumOfSubArray);
    }





    public static int selfResolution(int[] array){
        if (array == null || array.length == 0) {
            return Integer.MIN_VALUE;
        }


        int result = 0;
        int sum = array[0];

        for (int i = 1; i < array.length; i++) {

            sum = sum+array[i] > array[i] ? (sum + array[i]) : array[i];

            if (result < sum) {
                result = sum;
            }
        }

        return result;

    }












    public static int FindGreatestSumOfSubArray(int[] array) {
        /*动态规划，用res[i]表示以第i个元素结尾的最大和
        res[i]中最大者即为最大连续子序列的和*/
        if(array==null||array.length==0) {
            return Integer.MIN_VALUE;
        }
        int endAsI=array[0];
        int result=endAsI;
        for(int i=1;i<array.length;i++){
            endAsI=endAsI+array[i]>array[i]? endAsI+array[i] : array[i];
            if(endAsI>result) {
                result=endAsI;
            }
        }
        return result;
    }
}
