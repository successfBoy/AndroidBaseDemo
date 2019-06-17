package com.lpc.androidbasedemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/*
 * @author lipengcheng
 * create at  2019/1/14
 * description:
 */
public class TestActivity2 extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        button.setText("activity 2");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity2.this,TestActivity3.class));
            }
        });
        setContentView(button);

//        Message.obtain().sendToTarget();
//        Handler handler = new Handler();
//        handler.getLooper().quitSafely();
    }
}
