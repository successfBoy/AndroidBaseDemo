package com.lpc.androidbasedemo.initenttest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.lpc.androidbasedemo.activity.MainActivity;

/*
 * @author lipengcheng
 * create at  2019/5/18
 * description:
 */
public class FirstActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"------->"+ MainActivity.testNum,Toast.LENGTH_LONG).show();
    }

}
