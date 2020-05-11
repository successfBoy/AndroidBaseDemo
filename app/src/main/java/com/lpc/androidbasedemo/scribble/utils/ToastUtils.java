package com.lpc.androidbasedemo.scribble.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Copyright (c) 2015  All Rights Reserved.
 * Created by cherish on 15/9/21.
 * Email:wzyax@qq.com
 * Desc:消息提醒单例
 */
public class ToastUtils {
    private static Toast mToast;

    /**
     * 显示Toast
     *
     * @param context
     * @param msg
     */
    public static void show(Context context, CharSequence msg) {
        if (ToastUtils.mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }

        mToast.show();
    }


    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }

    /**
     * 取消显示Toast
     */
    public static void cancelToast() {
        if (ToastUtils.mToast != null) {
            mToast.cancel();
        }
    }
}
