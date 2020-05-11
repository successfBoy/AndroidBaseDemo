package com.lpc.androidbasedemo.scribble.utils;

/**
 * Created by wangzhiyuan on 2016/11/22.
 * desc:
 */

public enum Permission {
    AUTH_READ(1),
    AUTH_WRITE(2),
    AUTH_READWRITE(3);


    private int index;

    Permission(int idx) {
        this.index = idx;
    }

    public int getIndex() {
        return index;
    }
}