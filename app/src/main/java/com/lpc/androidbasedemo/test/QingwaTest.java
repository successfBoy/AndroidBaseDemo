package com.lpc.androidbasedemo.test;

/*
 * @author lipengcheng
 * create at  2019/1/27
 * description:
 */
public class QingwaTest {

    public static void main(String[] args) {
        int jumpFloorII = JumpFloorII(9);
        System.out.print("升级版青蛙需要跳 ："+jumpFloorII);
        int notDiGui = notDiGui(9);
        System.out.print("\n升级版青蛙需要跳 ："+notDiGui);
        int i = NumberOf1(3);
        System.out.print("\n二进制 ："+i);

        int sum_solution = Sum_Solution(5);
        System.out.print("\nsum_solution ："+sum_solution);

    }

    /**
     * 升级版青蛙跳台阶，可多跳
     * @param target
     * @return
     */
    public static int JumpFloorII(int target) {
        if (0 == target) {
            return 0;
        } else if (1 == target) {
            return 1;
        } else {
            return 2 * JumpFloorII(target - 1);
        }
    }

    public static int notDiGui(int n){
        int s= 0 , a ;
        for (int i = n-1; i < n&& i>0; i--) {
            if(i == 1){
                a= 1;
            }else {
                a=(i-1);

            }
            s += a;
        }
        return s;
    }

    public static int NumberOf1(int n) {
        int a = Math.abs(n);
        int count = 0;
        while(a>=1){
            if(a%2==1){
                count +=1;
            }
            a = a/2;
        }
        if(n > 0){
            count += 1;
        }
        return count;
    }

    public static int Sum_Solution(int n) {
        if(n == 1){
            return 1;
        }
        return n+Sum_Solution(n-1);
    }
}
