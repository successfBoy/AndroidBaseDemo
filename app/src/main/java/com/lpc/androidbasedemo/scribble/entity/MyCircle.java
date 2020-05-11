package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

// 圆框
public class MyCircle extends Action {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private float radius;

    MyCircle() {
        startX = 0;
        startY = 0;
        stopX = 0;
        stopY = 0;
        radius = 0;
    }

    public MyCircle(float x, float y, int size, int color) {
        super(color);
        startX = x;
        startY = y;
        stopX = x;
        stopY = y;
        radius = 0;
        this.size = size;
    }

    public MyCircle(BrushData brushData) {
        super(brushData);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
//        if(isRemote()&&brushData.getM_width()!=brushData.getM_height()){
        canvas.drawOval(new RectF(startX, startY, stopX, stopY), paint);
        updateUserNamePosition((int) stopX,(int) stopY);
//        }else
//        canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius,
//                paint);
    }

    public void move(float mx, float my) {
        stopX = mx;
        stopY = my;
        radius = (float) ((Math.sqrt((mx - startX) * (mx - startX) + (my - startY) * (my - startY))) / 2);
    }


    @Override
    public void updateAction() {
        super.updateAction();
        Point p = brushData.getM_points().get(0);
        startX = getRationx() * (p.x - brushData.getM_width() / 2);
        startY = getRationy() * (p.y - brushData.getM_height() / 2);
        stopX = getRationx() * (p.x + brushData.getM_width() / 2);
        stopY = getRationy() * (p.y + brushData.getM_height() / 2);
//        move(p.x * getRationx() + brushData.getM_width() / 2, p.y * getRationy() + brushData.getM_height() / 2);
        move(stopX, stopY);
    }
}
