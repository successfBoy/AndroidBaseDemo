package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.lpc.androidbasedemo.R;

/**
 * Created by wangzhiyuan on 2016/12/20.
 * desc:
 */

public class MyLaster extends Action {

    private static Bitmap bitmap;
    private Paint paint;

    public MyLaster() {
        super();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.doodle_ico_laser_pen_show);

    }

    private float startX;
    private float startY;



    @Override
    public void draw(Canvas canvas) {
            canvas.drawBitmap(bitmap,startX,startY,paint);
    }

    @Override
    public void move(float mx, float my) {

    }
    @Override
    public void updateAction() {
        super.updateAction();
        startX = (float) scribbleView.getWidth() / mouseMoveData.getWidth() * (mouseMoveData.x);
        //startY = (float) scribbleView.getHeight()/ mouseMoveData.getHeight() * (mouseMoveData.y-(bitmap.getHeight()));
        startY = (float) scribbleView.getWidth()/ mouseMoveData.getWidth() * (mouseMoveData.y-(bitmap.getHeight()));//以宽度为准
    }
}
