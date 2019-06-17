package com.lpc.androidbasedemo.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.LinearLayout;

import com.lpc.androidbasedemo.R;

/*
 * @author lipengcheng
 * create at  2019/1/9
 * description:
 */
public class TestActivity extends FragmentActivity {
    private LinearLayout mContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mContainer = findViewById(R.id.container);
        FragmentManager manager = getSupportFragmentManager();

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

}
