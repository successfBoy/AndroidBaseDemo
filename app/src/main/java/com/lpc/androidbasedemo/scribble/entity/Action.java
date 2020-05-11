package com.lpc.androidbasedemo.scribble.entity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lpc.androidbasedemo.scribble.ScribbleView;
import com.lpc.androidbasedemo.scribble.newservice.ScribbleManager;
import com.lpc.androidbasedemo.scribble.utils.Utils;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

//基础类
public abstract class Action {

    protected int id;
    protected int color;
    protected int size;
    protected BrushData brushData;
    protected MouseMoveData mouseMoveData;
    protected ScribbleView scribbleView;
    protected Context context;
    /**
     * 用户名绘制的坐标
     */
    protected int mUserNameX = -1, mUserNameY = -1;

    Action() {
        context = ScribbleManager.getsInstance().getContext();

        color = Color.BLACK;
    }

    public MouseMoveData getMouseMoveData() {
        return mouseMoveData;
    }

    public Action setMouseMoveData(MouseMoveData mouseMoveData) {
        this.mouseMoveData = mouseMoveData;
        return this;
    }

    public BrushData getBrushData() {
        return brushData;
    }

    public Action setBrushData(BrushData brushData) {
        this.brushData = brushData;
        return this;
    }

    Action(int color) {
        this.color = color;
    }

    Action(BrushData brushData) {
        this(brushData.getM_pen().getColor());
        this.brushData = brushData;
        context = ScribbleManager.getsInstance().getContext();
        if (context != null) {
            Resources r = context.getResources(); // 取得手機資源
            this.size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, //轉換dp值
                    brushData.getM_pen().getRude(), //dp值
                    r.getDisplayMetrics()) / 2;
        }
    }

    public Action setScribbleView(ScribbleView scribbleView) {
        this.scribbleView = scribbleView;
        return this;
    }

    public ScribbleView getScribbleView() {
        return this.scribbleView;
    }

    public abstract void draw(Canvas canvas);

    public abstract void move(float mx, float my);

    public void updateAction() {
    }


    float getCenterRationx(int x) {
        int w = Utils.displayMetrics().widthPixels;

        return (float) (w * 0.5 - (((brushData.getM_areaWidth() * 0.5 - x) * w) / brushData.getM_areaWidth()));

    }

    float getCenterRationy(int y) {
        int h = Utils.displayMetrics().heightPixels;
        int w = Utils.displayMetrics().widthPixels;

        return (float) (h * 0.5 - (((brushData.getM_areaHeight() * 0.5 - y) * w) / brushData.getM_areaWidth()));

    }

    float getRationx() {
        if (!isRemote()) {
            return 1;
        }

//        int w = Utils.displayMetrics().widthPixels;
        return (float) scribbleView.getWidth() / brushData.getM_areaWidth();
    }

    float getRationy() {
        if (!isRemote()) {
            return 1;
        }
//        int h = Utils.displayMetrics().heightPixels;

        //return (float) scribbleView.getHeight() / brushData.getM_areaHeight(); //changed 只按照"远程画布的宽度/本地画布宽度"来缩

        return getRationx();
    }

    public boolean isRemote() {
        return brushData != null;
    }

    /**
     * 记录用户名显示的位置
     *
     * @param stopX
     * @param stopY
     */
    public void updateUserNamePosition(final int stopX, final int stopY) {
        mUserNameX = stopX;
        mUserNameY = stopY;
    }

    /**
     * 修改用户名称现实的位置
     *
     * @param mDrawUserName
     * @return
     */
    public boolean drawUserName(TextView mDrawUserName) {
        if (mUserNameX == -1 && mUserNameY == -1) {
            return false;
        }
        int topMargin = 0;
        if (scribbleView != null) {
            if (scribbleView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                topMargin = ((ViewGroup.MarginLayoutParams) scribbleView.getLayoutParams()).topMargin;
            }
        }
        mDrawUserName.setX(mUserNameX);
        mDrawUserName.setY(mUserNameY + topMargin);
        mDrawUserName.invalidate();
        return true;
    }
}