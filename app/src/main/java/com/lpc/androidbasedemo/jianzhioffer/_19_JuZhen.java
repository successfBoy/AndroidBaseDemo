package com.lpc.androidbasedemo.jianzhioffer;

import java.util.ArrayList;

/**
 * @author : lipengcheng1
 * @date : 2020-04-23
 * desc:
 *
 * 顺时针打印矩阵
 *
 * 题目描述：
 *
 *   输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字，
 * 例如，如果输入如下4 X 4矩阵： 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16
 * 则依次打印出数字1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10.
 *
 *
 *
 *  1  2  3  4
 *  5  6  7  8
 *  9 10 11 12
 * 13 14 15 16
 *
 *   解题思路：
 *
 *   由于是按照从外到内的顺序依次打印，所以可以把矩阵想象成若干个圈，用一个循环来打印矩阵，
 * 每次打印矩阵中的一圈。假设矩阵的行数是row，列数是col，则每次都是从左上角开始遍历，
 * 而我们注意到左上角行标和列标总是相同的，假设是start，那么循环继续的条件就是row>start * 2 && col > start * 2。
 *
 *   而对于每一圈的打印，很自然便可以想到遵循从左到右，从上到下，从右到左，从下到上的顺序。
 * 但是这里需要注意的是最后一圈的打印，由于矩阵并不一定是方阵，最后一圈有可能退化为只有一行，
 * 只有一列，甚至只有一个数，因此要注意进行判断，避免重复打印。
 */
public class _19_JuZhen {
    static int[][] matrix = {
            {1,2,3,4},
            {5,6,7,8},
            {9,10,11,12},
            {13,14,15,16}
    };


    public static void main(String[] args) {
        printMatrix();
    }

    private static void printMatrix() {
        int row = 0;
        int col=matrix.length -1;


        while (row <= col){
            for (int i = row; i <=col; i++) {
                System.out.println(matrix[row][i]);
            }

            for (int i = row +1; i <=col; i++) {
                System.out.println(matrix[i][col]);
            }

            for (int i = col-1; i >= row ; i--) {
                System.out.println(matrix[col][i]);
            }

            for (int i = col -1 ; i > row; i--) {
                System.out.println(matrix[i][row]);
            }

            row ++;
            col --;
        }
    }


    public ArrayList<Integer> printMatrix(int [][] matrix) {
        ArrayList<Integer> res=new ArrayList<>();
        int row=matrix.length; //行数
        if(row==0) {
            return res;
        }
        int col=matrix[0].length;//列数
        for(int i=0;col>2*i&&row>2*i;i++){ //圈数，从外向内,循环结束条件需要注意
            //打印一圈
            int endY=col-1-i;
            int endX=row-1-i;

            //从左到右,第i行
            for(int j=i;j<=endY;j++) {
                res.add(matrix[i][j]);
            }
            //从上到下，第endY列
            for(int j=i+1;j<=endX;j++) {
                res.add(matrix[j][endY]);
            }
            //从右向左，第endX行
            if(endX>i) {
                for(int j=endY-1;j>=i;j--) {
                    res.add(matrix[endX][j]);
                }
            }
            //从下到上，第i列
            if(endY>i) {
                for(int j=endX-1;j>i;j--) {
                    res.add(matrix[j][i]);
                }
            }
        }
        return res;
    }


}
