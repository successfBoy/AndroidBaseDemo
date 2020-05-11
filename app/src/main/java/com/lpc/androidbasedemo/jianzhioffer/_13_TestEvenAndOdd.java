package com.lpc.androidbasedemo.jianzhioffer;

/**
 * @author : lipengcheng1
 * @date : 2020-04-23
 * desc:
 *
 * 调整数组顺序使奇数位于偶数前面
 *
 * 题目描述：
 *
 *   输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有的奇数位于数组的前半部分，
 * 所有的偶数位于数组的后半部分，并保证奇数和奇数，偶数和偶数之间的相对位置不变。
 *
 *   解题思路：
 *
 *   首先，如果不考虑奇数和奇数，偶数和偶数的相对位置，那么我们有一种双指针解法来求解，类似于快排，维护两个指针，
 * 第一个指针指向数组的第一个数字，第二个指针指向数组的最后一个数字。第一个指针向后移，第二个指针向前移，
 * 如果第一个指针指向偶数，第二个指针指向的是奇数，则交换着两个数字，接着继续移动直到两指针相遇。
 *
 *   上面的方法看似不错，但是对本题不适用，因为本题有相对位置不变的要求，直接交换会导致相对位置改变。
 * 因此，我们采用下面的思路来解决本题。
 *
 *   本题解法：对数组进行遍历，设置两个指针even和odd，even指向当前第一个偶数，odd从这个偶数之后开始查找，
 * 找到第一个奇数，此时为了相对位置不变，不能直接交换even和odd，而是将从even到odd-1的元素都依次向后移一个位置，
 * 将odd指向的那个奇数放到even的位置。然后再找下一个偶数，重复这一过程，最终就可以将奇数都放到偶数的前面，
 * 并且保证了相对位置的不变
 */
public class _13_TestEvenAndOdd {
    static int[] couts = {1, 24, 23, 54, 33, 4, 456, 532, 246, 91, 234, 54};

    public static void main(String[] args) {
        reOrderArray(couts);
        for (int i = 0; i < couts.length; i++) {
            System.out.println(couts[i]);
        }

    }


    public static void reOrderArray1(int[] array) {
        int len = array.length;
        if (len == 0) {
            return;
        }
        //奇数在前，偶数在后
        int e = 0;//偶数
        int o = 0;//奇数

        while (e<len){
            while (e < len && array[e]%2 !=0){
                e++;
            }

            o = e+1;
            while (o<len && array[o]%2 == 0) {//找到奇数
                o++;
            }

            if (e >= len || o>=len){
                break;
            }

            int temp = array[o];
            for (int i = o; i >e ; i--) {
                array[i] = array[i -1];
            }
            array[e] = temp;
        }
    }



    public static void reOrderArray(int[] array) {
        int len = array.length;
        int even = 0, odd = 0; //当前序列的第一个奇数和第一个偶数
        while (odd < len && even < len) {
            while (even < len && array[even] % 2 != 0) //找到第一个偶数even
            {
                even++;
            }
            odd = even + 1;
            //找偶数之后的第一个奇数
            while (odd < len && array[odd] % 2 == 0) {
                odd++;
            }
            if (odd >= len)  //注意判断，防止溢出
            {
                break;
            }
            //把奇数取出来，从even到odd-1的元素都向后移
            int temp = array[odd];
            for (int i = odd; i > even; i--) {
                array[i] = array[i - 1];
            }
            array[even] = temp; //奇数放在原来even的位置
            even++;
        }
    }
}
