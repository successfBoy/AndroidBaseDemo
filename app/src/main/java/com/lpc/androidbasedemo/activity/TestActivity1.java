package com.lpc.androidbasedemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lpc.androidbasedemo.initenttest.FirstActivity;

/*
 * @author lipengcheng
 * create at  2019/1/14
 * description:
 */
public class TestActivity1 extends Activity {
    private String TAG = TestActivity1.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        button.setText("activity 1");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity1.this, FirstActivity.class));
            }
        });
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        MyLinearlayout linearlayout = new MyLinearlayout(this);
        linearlayout.setClickable(true);
        linearlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "onTouch: ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "onTouch: ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "onTouch: ACTION_UP");
                        break;
                }
                return false;
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "button onTouch: ======================================");
                        Log.i(TAG, "button onTouch: ACTION_DOWN");
                        Log.i(TAG, "button onTouch: ======================================");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "button onTouch: ======================================");
                        Log.i(TAG, "button onTouch: ACTION_MOVE");
                        Log.i(TAG, "button onTouch: ======================================");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "button onTouch: ======================================");
                        Log.i(TAG, "button onTouch: ACTION_UP");
                        Log.i(TAG, "button onTouch: ======================================");
                        break;
                }
                return false;
            }
        });
        linearlayout.addView(button);
        setContentView(linearlayout);
    }

    class MyLinearlayout extends LinearLayout {

        public MyLinearlayout(Context context) {
            this(context, null);
        }

        public MyLinearlayout(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MyLinearlayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.i(TAG, "onInterceptTouchEvent: ");
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "onInterceptTouchEvent: ACTION_DOWN");
                    return false;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "onInterceptTouchEvent: ACTION_MOVE");
                    return true;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "onInterceptTouchEvent: ACTION_UP");
                    return false;
            }
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(TAG, "onTouchEvent: ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "onTouchEvent: ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "onTouchEvent: ACTION_UP");
                    break;
            }
            Log.i(TAG, "onTouchEvent: ");
            return super.onTouchEvent(event);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            Log.i(TAG, "dispatchTouchEvent: ");
            return super.dispatchTouchEvent(ev);
        }
    }
}
