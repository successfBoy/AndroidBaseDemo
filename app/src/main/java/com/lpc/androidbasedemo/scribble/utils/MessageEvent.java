package com.lpc.androidbasedemo.scribble.utils;

/**
 * Created by wangzhiyuan on 2017/3/13.
 * desc:
 */

public class MessageEvent<T> {
    private final T t;

    public T getT() {
        return t;
    }

    public MessageEvent(T t) {
        this.t = t;
    }
}
