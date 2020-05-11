package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

//三角形
//三角形
public class MyTriangle extends Action {
    float startX;
    float startY;
    float stopX;
    float stopY;
    //    float radius;
    Paint mPaint;

    public MyTriangle(float x, float y, int size, int color) {
        super(color);
        startX = x;
        startY = y;
        stopX = x;
        stopY = y;
//        radius = 0;
        this.size = size;

    }

    public MyTriangle(BrushData brushData) {
        super(brushData);
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(size);
        Path mPath = new Path();

        int width = (int) (stopX - startX);
        mPath.moveTo(startX + (width / 2), startY);
        mPath.lineTo(startX, stopY);
        mPath.lineTo(stopX, stopY);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        updateUserNamePosition((int) stopX,(int) stopY);
    }

    @Override
    public void move(float mx, float my) {

        stopX = mx;
        stopY = my;

//        radius = (float) ((Math.sqrt((mx - startX) * (mx - startX) + (my - startY) * (my - startY))) / 2);
    }

    @Override
    public void updateAction() {
        super.updateAction();
        Point p = brushData.getM_points().get(0);
        startX = getRationx() * (p.x - brushData.getM_width() / 2);
        startY = getRationy() * (p.y - brushData.getM_height() / 2);
        move(getRationx() * (p.x + brushData.getM_width() / 2), getRationy() * (p.y + brushData.getM_height() / 2));

    }
}
