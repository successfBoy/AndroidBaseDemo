package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */


// 圆饼
public class MyFillCircle extends Action {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private float radius;
    private int size;

    MyFillCircle() {
        startX = 0;
        startY = 0;
        stopX = 0;
        stopY = 0;
        radius = 0;
    }

    public MyFillCircle(float x, float y, int size, int color) {
        super(color);
        startX = x;
        startY = y;
        stopX = x;
        stopY = y;
        radius = 0;
        this.size = size;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius,
                paint);
    }

    public void move(float mx, float my) {
        stopX = mx;
        stopY = my;
        radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                + (my - startY) * (my - startY))) / 2);
    }

}
