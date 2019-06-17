package com.sixfriend.shiyou.mylibrary.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/*
 * @author lipengcheng
 * create at  2019-06-17
 * description:
 */
public class PluginActivity extends Activity implements IPlugin{
    private Activity mPoxyActivity;
    private int FROM = FROM_INTERNAL;

    @Override
    public void attach(Activity poxyActivity) {
        this.mPoxyActivity = poxyActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            FROM = savedInstanceState.getInt("FROM");
        }
        if(FROM_INTERNAL == FROM){
            super.onCreate(savedInstanceState);
            mPoxyActivity = this;
        }
    }


    @Override
    public void setContentView(int layoutResID) {
        if(FROM == FROM_INTERNAL){
            super.setContentView(layoutResID);
        }else{
            mPoxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void onPause() {
        if(FROM == FROM_INTERNAL){
            super.onPause();
        }
    }
}
