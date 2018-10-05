package com.lpc.androidbasedemo.reactnative;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.lpc.androidbasedemo.reactnative.module.MyToastModules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/7/18
 * description:
 */
public class MyReactPackage implements ReactPackage{

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new MyToastModules(reactContext));
        return modules;
    }


    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }


    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
