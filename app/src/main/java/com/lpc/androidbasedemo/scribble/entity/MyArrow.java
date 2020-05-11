package com.lpc.androidbasedemo.scribble.entity;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 画箭头
 */
public class MyArrow extends Action {
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    Paint paint;
    Canvas mCanvas;

    MyArrow(float x, float y, int size, int color) {
        super(color);
        startX = x;
        startY = y;
        stopX = x;
        stopY = y;
        this.size = size;
    }

    public MyArrow(BrushData brushData) {
        super(brushData);
    }


    @Override
    public void draw(Canvas canvas) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        mCanvas = canvas;
        drawAL((int) startX, (int) startY, (int) stopX, (int) stopY);
    }

    @Override
    public void move(float mx, float my) {
        stopX = mx * getRationx();
        stopY = my * getRationy();
    }

    /**
     * 画箭头
     *
     * @param startX 开始位置x坐标
     * @param startY 开始位置y坐标
     * @param endX   结束位置x坐标
     * @param endY   结束位置y坐标
     */
    public void drawAL(int startX, int startY, int endX, int endY) {
        double lineLength = Math.sqrt(Math.pow(Math.abs(endX - startX), 2) + Math.pow(Math.abs(endY - startY), 2));//线当前长度
        double H;// 箭头高度
        double L;// 箭头长度
        H = size * 1.5;
        L = size;

        double arrawAngle = Math.atan(L / H); // 箭头角度
        double arraowLen = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] pointXY1 = rotateAndGetPoint(endX - startX, endY - startY, arrawAngle, true, arraowLen);
        double[] pointXY2 = rotateAndGetPoint(endX - startX, endY - startY, -arrawAngle, true, arraowLen);
        int x3 = (int) (endX - pointXY1[0]);//(x3,y3)为箭头一端的坐标
        int y3 = (int) (endY - pointXY1[1]);
        int x4 = (int) (endX - pointXY2[0]);//(x4,y4)为箭头另一端的坐标
        int y4 = (int) (endY - pointXY2[1]);

        //画三角形
//		Log.i("Action",x3+","+y3+","+endX+","+endY+","+x4+","+y4);
        if (x3 != 0 && x4 != 0 && y3 != 0 && y4 != 0) {
            Path mPath = new Path();
            mPath.moveTo(x3, y3);
            mPath.lineTo(endX, endY);
            mPath.lineTo(x4, y4);
            mPath.close();
            mCanvas.drawPath(mPath, paint);
        }
    }

    /**
     * 矢量旋转函数，计算末点的位置
     *
     * @param x       x分量
     * @param y       y分量
     * @param ang     旋转角度
     * @param isChLen 是否改变长度
     * @param newLen  箭头长度长度
     * @return 返回末点坐标
     */
    public double[] rotateAndGetPoint(int x, int y, double ang, boolean isChLen, double newLen) {
        double pointXY[] = new double[2];
        double vx = x * Math.cos(ang) - y * Math.sin(ang);
        double vy = x * Math.sin(ang) + y * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            pointXY[0] = vx / d * newLen;
            pointXY[1] = vy / d * newLen;
        }
        return pointXY;
    }
   /* @Override
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
//                reset(p.x,p.y);
                startX = p.x * getRationx();
                startY = p.y * getRationy();
            } else if (i == 1) {
                move(p.x, p.y);

            }
        }
    }
}
