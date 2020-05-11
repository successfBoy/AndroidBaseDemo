package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

// 点
public class MyPoint extends Action {
    private float x;
    private float y;

    public MyPoint(float px, float py, int color) {
        super(color);
        this.x = px;
        this.y = py;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void move(float mx, float my) {

    }
}