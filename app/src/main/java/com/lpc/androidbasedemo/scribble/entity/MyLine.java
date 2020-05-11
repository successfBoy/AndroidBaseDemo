package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

public class MyLine extends Action {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    MyLine() {
        startX = 0;
        startY = 0;
        stopX = 0;
        stopY = 0;
    }

    public MyLine(float x, float y, int size, int color) {
        super(color);
        startX = x;
        startY = y;
        stopX = x;
        stopY = y;
        this.size = size;
    }

    public MyLine(BrushData brushData) {
        super(brushData);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        updateUserNamePosition((int) stopX, (int) stopY);
    }

    public void move(float mx, float my) {
        stopX = mx * getRationx();
        stopY = my * getRationy();
    }
    /*@Override
    public void reset(float mx, float my) {
        startX = mx * getRationx();
        startY = my * getRationy();

    }*/

    @Override
    public void updateAction() {
        super.updateAction();
        for (int i = 0; i < brushData.getM_points().size(); i++) {
            Point p = brushData.getM_points().get(i);
            if (i == 0) {
                startX = p.x * getRationx();
                startY = p.y * getRationy();
            } else if (i == 1) {
                move(p.x, p.y);

            }
        }
    }
}
