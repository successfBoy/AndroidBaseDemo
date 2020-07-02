package com.lpc.widget;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;

/**
 * @author : lipengcheng1
 * @date : 2020/7/1
 * desc:
 */
public class WidgetListActivity extends Activity {
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mHandler.sendEmptyMessageDelayed(0 , 1000);
        }
    };

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widgetlist);
        ClockView view = findViewById(R.id.clockview);
        Button button = findViewById(R.id.pressbutton);

        view.start();
    }


}
