package com.lpc.androidbasedemo.scribble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.scribble.view.MyAudioWave;


public class BarAnimaActivity extends AppCompatActivity {

    private MyAudioWave myAF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doodle_activity_bar_anima);
         myAF = (MyAudioWave) findViewById(R.id.myaf);
        myAF.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myAF.stop();
    }
}
