package com.lpc.androidbasedemo.scribble;

import com.lpc.androidbasedemo.scribble.utils.StrokeType;

/**
 * Created by wangzhiyuan on 2016/11/21.
 * desc:
 */

public class Pen {
    int color;//颜色
    StrokeType type;//类型
    int rude;//粗细

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getRude() {
        return rude;
    }

    public void setRude(int rude) {
        this.rude = rude;
    }

    public StrokeType getType() {
        return type;
    }

    public void setType(StrokeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Pen{" +
                "color=" + color +
                ", type=" + type +
                ", rude=" + rude +
                '}';
    }
}
