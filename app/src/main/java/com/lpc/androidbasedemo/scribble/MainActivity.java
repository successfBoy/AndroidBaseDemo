package com.lpc.androidbasedemo.scribble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.scribble.view.WaveFormView;


public class MainActivity extends AppCompatActivity {

    private WaveFormView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doodle_activity_main);
        view = (WaveFormView) this.findViewById(R.id.wave);
        view.updateAmplitude(1, true);
    }
}
