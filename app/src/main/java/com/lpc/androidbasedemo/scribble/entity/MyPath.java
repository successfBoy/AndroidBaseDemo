package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

public class MyPath extends Action {
    private Path path;
    private float stopX, stopY;

    public MyPath() {
        path = new Path();
        size = 5;
        color = Color.BLACK;
    }

    public MyPath(float x, float y, int size, int color) {
        super(color);
        path = new Path();
        this.size = size;
        path.moveTo(x, y);
        path.lineTo(x, y);
    }

    public MyPath(BrushData brushData, float x, float y) {
        super(brushData);
        path = new Path();
//        this.size = brushData.getM_pen().getRude();
//        Point p = brushData.getM_points().get(0);
        path.moveTo(x, y);
        path.lineTo(x, y);

    }

    public MyPath(BrushData brushData) {
        super(brushData);
        path = new Path();
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        if (brushData.m_points.size() == 1) {
            Point p = brushData.m_points.get(0);
            canvas.drawPoint(p.x * getRationx(), p.y * getRationy(), paint);
        } else {
            canvas.drawPath(path, paint);
            updateUserNamePosition((int) stopX, (int) stopY);
        }
    }

    public void move(float mx, float my) {
        path.lineTo(mx * getRationx(), my * getRationy());
        stopX = mx * getRationx();
        stopY = my * getRationy();
    }

    /*@Override
    public void reset(float mx, float my) {
        path.reset();
        path.moveTo(mx * getRationx(), my * getRationy());

    }*/

    @Override
    public void updateAction() {
        super.updateAction();
        for (int i = 0; i < brushData.m_points.size(); i++) {
            Point p = brushData.m_points.get(i);
            if (i == 0) {
                path.reset();
                path.moveTo(p.x * getRationx(), p.y * getRationy());
            }
            move(p.x, p.y);
        }
    }
}