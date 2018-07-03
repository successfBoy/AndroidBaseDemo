package com.lpc.androidbasedemo.reactnative;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.lpc.androidbasedemo.R;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/7/2
 * description:
 */
public class ReactNativeActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler{
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactivity);
        mReactRootView = new ReactRootView(this);
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setBundleAssetName("index.android.bundle")
                //需和index.android 的路径一致
                .setJSMainModuleName("jsbundles/index.android")
                .addPackage(new MainReactPackage())
                .setUseDeveloperSupport(true)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        mReactRootView.startReactApplication(mReactInstanceManager,"MyReactNativeApp",null);
        ((LinearLayout)findViewById(R.id.layout)).addView(mReactRootView);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                return;
            }
        }
    }

    /**
     * By default, all onBackPress() calls should not execute the default backpress handler and should
     * instead propagate it to the JS instance. If JS doesn't want to handle the back press itself,
     * it shall call back into native to invoke this function which should execute the default handler
     */
    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mReactInstanceManager != null){
            mReactInstanceManager.onHostPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mReactInstanceManager != null){
            mReactInstanceManager.onHostResume(this,this);
        }
    }

    @Override
    public void onBackPressed() {
        if(mReactInstanceManager != null){
            mReactInstanceManager.onBackPressed();
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null){
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReactInstanceManager != null){
            mReactInstanceManager.onHostDestroy(this);
        }
    }
}
