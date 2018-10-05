package com.lpc.androidbasedemo.reactnative.module;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/8/1
 * description: toast 原生组件
 */
public class MyToastModules extends ReactContextBaseJavaModule{
    private final static String MODULES_NAME_TOAST="MyToastModules";
    private static final String TOAST_LONG_KEY = "LONG" ;
    private static final String TOAST_SHORT_KEY = "SHORT" ;
    public MyToastModules(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULES_NAME_TOAST;
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String ,Object> constants = new HashMap<>();
        constants.put(TOAST_LONG_KEY, Toast.LENGTH_LONG) ;
        constants.put(TOAST_SHORT_KEY, Toast.LENGTH_SHORT) ;
        return constants;
    }

    @ReactMethod
    public void show(String text, int duration){
        Toast.makeText(getReactApplicationContext(),text, duration).show();

    }
}
