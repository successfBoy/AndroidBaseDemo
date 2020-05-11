package com.lpc.androidbasedemo.jianzhioffer;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author : lipengcheng1
 * @date : 2020-04-23
 * 题目描述：
 *
 *   数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字。
 *   例如：输入如下所示的一个长度为9的数组{1,2,3,2,2,2,5,4,2}。
 * 由于数字2在数组中出现了5次，超过数组长度的一半，因此输出2。如果不存在则输出0。
 *
 *   解题思路：
 *
 *   本题有以下三种方法可解：
 *
 *   方法一：首先对数组进行排序，在一个有序数组中，次数超过一半的必定是中位数，那么可以直接取出中位数，然后遍历数组，
 * 看中位数是否出现次数超过一半，这取决于排序的时间复杂度，最快为O(nlogn)。
 *
 *   方法二：遍历数组，用 HashMap 保存每个数出现的次数，这样可以从map中直接判断是否有超过一半的数字，
 * 这种算法的时间复杂度为O(n)，但是这个性能提升是用O(n)的空间复杂度换来的。
 *
 *   方法三（最优解法）：根据数组特点得到时间复杂度为O(n)的算法。根据数组特点，
 * 数组中有一个数字出现的次数超过数组长度的一半，也就是说它出现的次数比其他所有数字出现的次数之和还要多。
 * 因此，我们可以在遍历数组的时候设置两个值：一个是数组中的数result，另一个是出现次数times。
 * 当遍历到下一个数字的时候，如果与result相同，则次数加1,不同则次数减一，
 * 当次数变为0的时候说明该数字不可能为多数元素，将result设置为下一个数字，次数设为1。这样，当遍历结束后，
 * 最后一次设置的result的值可能就是符合要求的值（如果有数字出现次数超过一半，则必为该元素，否则不存在），
 * 因此，判断该元素出现次数是否超过一半即可验证应该返回该元素还是返回0。这种思路是对数组进行了两次遍历，复杂度为O(n)。
 */
public class _28_MoreThanHalfNum {

    static int[] a = {1,2,3,2,2,2,5,4,2};

    public static void main(String[] args) {
        findMoreThanHalfNum1();

        int i = self(a);
        System.out.println(i);
    }

    private static void findMoreThanHalfNum1() {
        Map<Integer , Integer> map = new HashMap<>();
        for (int i = 0; i < a.length; i++) {
            if (map.containsKey(a[i])){
                int i1 = map.get(a[i]) + 1;
                map.put(a[i],i1);
            }else {
                map.put(a[i],1);
            }
        }

        Map.Entry<Integer, Integer> entry = null;
        Set<Map.Entry<Integer, Integer>> entries = map.entrySet();
        for (Map.Entry<Integer, Integer> e :entries) {
            if (entry == null){
                entry = e;
            }else {
                if (e.getValue()> entry.getValue()){
                    entry = e;
                }
            }
        }

        if (entry != null){
            System.out.println("this is "+entry.getKey() + " 出现的次数：" +entry.getValue());
        } else {
            System.out.println("not exist");
        }
    }


    //思路2：用hashmap保存每个数出现的次数
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int MoreThanHalfNum_Solution2(int [] array) {
        if(array==null) {
            return 0;
        }
        Map<Integer,Integer> res=new HashMap<>();
        int len = array.length;
        for(int i=0;i<array.length;i++){
            res.put(array[i],res.getOrDefault(array[i],0)+1);
            if(res.get(array[i])>len/2)
                return array[i];
        }
        return 0;
    }
    //思路3：根据数组特点得到时间复杂度为O(n)的算法
    public static int MoreThanHalfNum_Solution(int [] array) {
        if(array==null||array.length==0) {
            return 0;
        }
        int len = array.length;
        int result=array[0];
        int times=1;
        for(int i=1;i<len;i++){
            if(times==0){
                result=array[i];
                times=1;
                continue;
            }

            if(array[i]==result) {
                times++;
            } else {
                times--;
            }
        }
        //检查是否符合
        times=0;
        for(int i=0;i<len;i++){
            if(array[i]==result) {
                times++;
            }
            if(times>len/2) {
                return result;
            }
        }
        return 0;
    }



    public static  int self(int[] array){
        if (array == null || array.length == 0) {
            return 0;
        }

        int result = array[0];
        int len = array.length;
        int times = 1;
        for (int i = 0; i < len; i++) {
            if (times == 0){
                result = array[i];
                times = 1;
                continue;
            }

            if (array[i] == result){
                times ++;
            } else {
                times --;
            }
        }


        int time = 0;
        for (int i = 0; i < len; i++) {
            if (array[i] == result){
                time ++;
            }

            if (time > len/2)return result;
        }
        return 0;
    }
}
