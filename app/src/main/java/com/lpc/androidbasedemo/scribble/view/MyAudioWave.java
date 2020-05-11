package com.lpc.androidbasedemo.scribble.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.scribble.utils.DisplayUtil;

/**
 * Created by wangzhiyuan on 2017/1/5.
 * desc:http://www.jb51.net/article/92928.htm
 */

public class MyAudioWave extends View {
    private static final String TAG = MyAudioWave.class.getName();
    private int SPACE = 250;// 间隔取样时间

    // 音频矩形的数量
    private int mRectCount;
    // 音频矩形的画笔
    private Paint mRectPaint;
    // 渐变颜色的两种
    private int topColor, downColor;
    // 音频矩形的宽和高
    private int mRectWidth, mRectHeight;
    // 偏移量
    private int offset;
    // 频率速度
    private int mSpeed;

    private VolumeListener volumeListener;
    private double voumle;

    public void setVolumeListener(VolumeListener volumeListener) {
        this.volumeListener = volumeListener;
    }

    public MyAudioWave(Context context) {
        super(context);
    }

    public MyAudioWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaint(context, attrs);
    }

    public MyAudioWave(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaint(context, attrs);
    }

    public void setPaint(Context context, AttributeSet attrs) {
// 将属性存储到TypedArray中
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.doodle_MyAudioWave);
        mRectPaint = new Paint();
// 添加矩形画笔的基础颜色
        mRectPaint.setColor(ta.getColor(R.styleable.doodle_MyAudioWave_doodle_AFTopColor,
                ContextCompat.getColor(context, R.color.doodle_top_color)));
// 添加矩形渐变色的上面部分
        topColor = ta.getColor(R.styleable.doodle_MyAudioWave_doodle_AFTopColor,
                ContextCompat.getColor(context, R.color.doodle_top_color));
// 添加矩形渐变色的下面部分
        downColor = ta.getColor(R.styleable.doodle_MyAudioWave_doodle_AFDownColor,
                ContextCompat.getColor(context, R.color.doodle_down_color));
// 设置矩形的数量
        mRectWidth = DisplayUtil.px2dip(getContext(), ta.getInt(R.styleable.doodle_MyAudioWave_doodle_AFWidth, 18));
// 设置重绘的时间间隔，也就是变化速度
        mSpeed = ta.getInt(R.styleable.doodle_MyAudioWave_doodle_AFSpeed, 300);
// 每个矩形的间隔
        offset = DisplayUtil.px2dip(getContext(), ta.getInt(R.styleable.doodle_MyAudioWave_doodle_AFOffset, 5));
// 回收TypeArray
        ta.recycle();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
   /*     Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = cf.orientation ; //获取屏幕方向
        if(ori == Configuration.ORIENTATION_LANDSCAPE){
            //横屏
            offset =5;
        }else if(ori == Configuration.ORIENTATION_PORTRAIT){
            //竖屏
            offset = 15;
        }*/
// 渐变效果
        LinearGradient mLinearGradient;
// 画布的宽
        int mWidth;
// 获取画布的宽
        mWidth = getWidth();
// 获取矩形的最大高度
        mRectHeight = getHeight();
// 获取单个矩形的宽度(减去的部分为到右边界的间距)
        mRectCount = (mWidth - offset) / mRectWidth;
// 实例化一个线性渐变
        mLinearGradient = new LinearGradient(
                0,
                0,
                mRectWidth,
                mRectHeight,
                topColor,
                downColor,
                Shader.TileMode.CLAMP
        );
// 添加进画笔的着色器
        mRectPaint.setShader(mLinearGradient);
    }

    public void setmRectHeight(double voumle) {
        this.voumle = voumle;
        Log.d(TAG, "setmRectHeight: " + voumle);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        double mRandom;
        float currentHeight;
        for (int i = 0; i < mRectCount; i++) {
// 由于只是简单的案例就不监听音频输入，随机模拟一些数字即可
            mRandom = getRandom();
//            voumle = volumeListener.onVolumeCallback();
            currentHeight = (float) (voumle * 50);
//            Log.d(TAG, "onDraw: before "+currentHeight);

            currentHeight = currentHeight >= mRectHeight ? currentHeight % mRectHeight : currentHeight;
            currentHeight += getRandom();
//            Log.d(TAG, "onDraw: after "+currentHeight);


// 矩形的绘制是从左边开始到上、右、下边（左右边距离左边画布边界的距离，上下边距离上边画布边界的距离）
            canvas.drawRect(
                    (float) (mRectWidth * i + offset),
                    mRectHeight / 2 - (currentHeight / 2),
                    (float) (mRectWidth * (i + 1)),
                    mRectHeight / 2 + (currentHeight / 2),
                    mRectPaint
            );
        }
// 使得view延迟重绘
//        postInvalidateDelayed(mSpeed);
    }

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
        }
    };

    private final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            //根据mHandler发送what的大小决定话筒的图片是哪一张
            //说话声音越大,发送过来what值越大
            invalidate();

        }

        ;
    };

    private double getRandom() {
        double random = Math.random();
        if (random == 0) {
            getRandom();
        }
        return random * 100 + 20;
    }

    private void updateMicStatus() {
        if (volumeListener != null) {
            voumle = volumeListener.onVolumeCallback();
//            Log.d(TAG, "updateMicStatus: "+voumle);
        }
        //我对着手机说话声音最大的时候，db达到了35左右，
        mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        //所以除了2，为的就是对应14张图片
        mHandler.sendEmptyMessage(0);
    }

    public void start() {

        updateMicStatus();
    }

    public void stop() {
        setVolumeListener(null);
        mHandler.removeCallbacks(null);
    }

    public interface VolumeListener {
        float onVolumeCallback();
    }
}