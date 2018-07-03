package com.lpc.androidbasedemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.algorithmic.view.activity.AlgorithmicActivity;
import com.lpc.androidbasedemo.reactnative.ReactNativeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn =(Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ReactNativeActivity.class));
            }
        });
    }
}
