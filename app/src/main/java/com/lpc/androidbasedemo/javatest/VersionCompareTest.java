package com.lpc.androidbasedemo.javatest;

/**
 * @author : lipengcheng1
 * @date : 2020-05-22
 * desc:
 */
public class VersionCompareTest {

    public static void main(String[] args) {
//        int compare = compare("22.21.55.34", "12.0.45");
//        System.out.println(compare);
        String s = addBinary("100",
                "110010");
        System.out.println(s);
    }

    private static int compare(String version1 , String version2){
        String[] sp1 = version1.split("\\.");
        String[] sp2 = version2.split("\\.");



        for (int i = 0,j=0; i < (sp1.length>sp2.length?sp1.length :sp2.length); i++) {
            if (i<sp2.length && i<sp1.length){
                if (Integer.valueOf(sp1[i])>Integer.valueOf(sp2[i])){
                    return 1;
                } else if(Integer.valueOf(sp1[i])<Integer.valueOf(sp2[i])){
                    return -1;
                }
            } else {

            }
        }

        return 0;
    }

    /**
     * 10110
     * 11111
     * ------
     *    101
     * @param a
     * @param b
     * @return
     */
    public static String addBinary(String a, String b) {
        String result = "";
        int aLength = a.length();
        int bLength = b.length();
        int cha = Math.abs(aLength - bLength);

        if (aLength < bLength) {
            String temp = a;
            a = b;
            b = temp;

        }

        aLength = a.length();

        boolean jinwei = false;
        for (int i = aLength - 1; i >= 0; i--) {
            int bIndex = i - cha;
            int aS  = a.charAt(i) - '0';
            if (bIndex >= 0) {
                int bS = b.charAt(bIndex) - '0';
                int sum = aS + bS + (jinwei? 1: 0);
                if (sum == 2 ){
                    result = "0" + result;
                    jinwei = true;
                } else if (sum < 2){
                    result = sum + result;
                    jinwei = false;
                } else {
                    result = "1" + result;
                    jinwei = true;
                }
            } else {
                if (jinwei){
                    int i1 = 1 + aS;
                    if (i1>1){
                        result = "0"+result;
                        jinwei = true;
                    } else {
                        result = i1 + result;
                        jinwei = false;
                    }
                } else {
                    result = aS + result;
                    jinwei = false;
                }
            }
        }

        if (jinwei){
            result = "1"+ result;
        }

        return result;
    }

    public int threeSumClosest(int[] nums, int target) {

        return 0;
    }
}
