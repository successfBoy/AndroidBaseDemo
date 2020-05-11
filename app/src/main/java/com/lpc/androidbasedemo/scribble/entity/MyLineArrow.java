package com.lpc.androidbasedemo.scribble.entity;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

import android.graphics.Canvas;

/**
 * 画直线加箭头
 */
public class MyLineArrow extends Action {

    private Action line;
    private Action arrow;

    public MyLineArrow(float x, float y, int size, int color) {
        super(color);
        line = new MyLine(x, y, size, color);
        arrow = new MyArrow(x, y, size, color);
    }

    public MyLineArrow(BrushData brushData) {
        super(brushData);
    }

    @Override
    public void draw(Canvas canvas) {
        line.draw(canvas);
        arrow.draw(canvas);
        updateUserNamePosition(line.mUserNameX, line.mUserNameY);
    }

    @Override
    public void move(float mx, float my) {
        line.move(mx, my);
        arrow.move(mx, my);
    }


    @Override
    public void updateAction() {
        super.updateAction();
        line = new MyLine(brushData).setScribbleView(scribbleView);
        line.updateAction();
        arrow = new MyArrow(brushData).setScribbleView(scribbleView);
        arrow.updateAction();
    }
}
