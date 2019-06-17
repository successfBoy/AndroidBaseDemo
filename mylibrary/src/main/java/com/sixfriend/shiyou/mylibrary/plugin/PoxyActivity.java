package com.sixfriend.shiyou.mylibrary.plugin;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;

/*
 * @author lipengcheng
 * create at  2019-06-17
 * description:
 */
public class PoxyActivity extends Activity {
    private final static String TAG = PoxyActivity.class.getSimpleName();
    private String mClassName;
    private PluginApk mPluginApk;
    private IPlugin mIPlugin;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        button.setText("1111111");
        setContentView(button);
        mClassName = getIntent().getStringExtra("className");
        mPluginApk = PluginManager.getInstance().getPluginApk();
        launchPluginActivity();
    }

    private void launchPluginActivity() {
        if(null == mPluginApk){
            Log.e(TAG, "launchPluginActivity: ");
        }
        try {
            Class<?> aClass = mPluginApk.mDexClassLoader.loadClass(mClassName);
            Object object = aClass.newInstance();
            if(object instanceof IPlugin){
                mIPlugin = (IPlugin) object;
                mIPlugin.attach(this);
                Bundle bundle = new Bundle();
                bundle.putInt("FROM",IPlugin.FROM_EXTERNAL);
                mIPlugin.onCreate(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {

        return mPluginApk == null ?super.getResources(): mPluginApk.mResources;
    }

    @Override
    public AssetManager getAssets() {
        return mPluginApk == null ?super.getAssets(): mPluginApk.mAssetManager;
    }

    @Override
    public ClassLoader getClassLoader() {
        return mPluginApk == null ? super.getClassLoader() : mPluginApk.mDexClassLoader;
    }
}
