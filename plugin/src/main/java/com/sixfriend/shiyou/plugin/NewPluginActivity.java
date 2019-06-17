package com.sixfriend.shiyou.plugin;

import android.app.Activity;
import android.os.Bundle;

import com.sixfriend.shiyou.mylibrary.plugin.PluginActivity;

public class NewPluginActivity extends PluginActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
