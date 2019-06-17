package com.lpc.androidbasedemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import dalvik.system.DexClassLoader;

/*
 * @author lipengcheng
 * create at  2019/1/14
 * description:
 */
public class TestActivity3 extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        button.setText("activity 3");
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TestActivity3.this,TestActivity2.class));
//            }
//        });
        setContentView(button);
//        DexClassLoader classLoader = new DexClassLoader()
    }
}
