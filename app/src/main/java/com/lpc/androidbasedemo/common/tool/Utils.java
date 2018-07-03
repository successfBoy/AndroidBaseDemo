package com.lpc.androidbasedemo.common.tool;

import android.content.Context;
import android.view.WindowManager;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/29
 * description:
 */
public class Utils {
    public static int dp2px(Context context, int value) {
        if(context != null){
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (value * scale + 0.5f);
        }
        return 0;
    }

    public static int getHeight(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();
    }
}
