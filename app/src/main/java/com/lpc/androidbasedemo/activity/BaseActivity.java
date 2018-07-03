package com.lpc.androidbasedemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/27
 * description:
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected abstract void initData();
    public abstract int getLayoutId() ;
    public abstract void initView();
    final public Context getContext(){
        return this;
    }
}
