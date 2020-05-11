package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

// 方块
public class MyFillRect extends Action {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private int size;

    MyFillRect() {
        startX = 0;
        startY = 0;
        stopX = 0;
        stopY = 0;
    }

    public MyFillRect(float x, float y, int size, int color) {
        super(color);
        startX = x;
        startY = y;
        stopX = x;
        stopY = y;
        this.size = size;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawRect(startX, startY, stopX, stopY, paint);
    }

    public void move(float mx, float my) {
        stopX = mx;
        stopY = my;
    }
}