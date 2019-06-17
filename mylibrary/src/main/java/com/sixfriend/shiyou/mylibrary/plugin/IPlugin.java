package com.sixfriend.shiyou.mylibrary.plugin;

import android.app.Activity;
import android.os.Bundle;

/*
 * @author lipengcheng
 * create at  2019-06-17
 * description:
 */
public interface IPlugin {
    int FROM_INTERNAL = 0;
    int FROM_EXTERNAL = 1;
    void attach(Activity poxyActivity);
    void onCreate(Bundle savedInstanceState);
    void onPause();
}
