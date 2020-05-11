package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

// 方框
public class MyRect extends Action {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    MyRect() {
        startX = 0;
        startY = 0;
        stopX = 0;
        stopY = 0;
    }

    public MyRect(float x, float y, int size, int color) {
        super(color);
        startX = x;
        startY = y;
        stopX = x;
        stopY = y;
        this.size = size;
    }

    public MyRect(BrushData brushData) {
        super(brushData);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawRect(startX, startY, stopX, stopY, paint);
        updateUserNamePosition((int) stopX,(int) stopY);
    }

    public void move(float mx, float my) {
        stopX = mx;
        stopY = my;
    }

    @Override
    public void updateAction() {
        super.updateAction();
        Point p = brushData.getM_points().get(0);

        startX = getRationx() * (p.x - brushData.getM_width() / 2);
        startY = getRationy() * (p.y - brushData.getM_height() / 2);
        stopX = getRationx() * (p.x + brushData.getM_width() / 2);
        stopY = getRationy() * (p.y + brushData.getM_height() / 2);

    }
}
