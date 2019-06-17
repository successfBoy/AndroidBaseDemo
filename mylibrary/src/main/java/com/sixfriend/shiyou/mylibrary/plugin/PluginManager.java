package com.sixfriend.shiyou.mylibrary.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/*
 * @author lipengcheng
 * create at  2019-06-17
 * description:
 */
public class PluginManager {
    private PluginApk mPluginApk;
    private Context mContext;
    private PluginManager(){}
    public static PluginManager getInstance(){
        return Holder.sPluginManager;
    }

    public void init(Context context){
        this.mContext = context;
    }


    public void loadApk(String apkPath){
        File file = new File(apkPath);
        boolean exists = file.exists();
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES|PackageManager.GET_SERVICES);
        if(null == packageInfo){
            return;
        }
        DexClassLoader dexClassLoader = createDexClassLoader(apkPath);
        AssetManager am = createAssetManager(apkPath);
        Resources resources = createResource(am);
        mPluginApk = new PluginApk(dexClassLoader,resources,packageInfo);
    }

    private Resources createResource(AssetManager assetManager) {
        Resources resources = mContext.getResources();
        return new Resources(assetManager,resources.getDisplayMetrics(),resources.getConfiguration());
    }

    private AssetManager createAssetManager(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            method.invoke(assetManager,apkPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private DexClassLoader createDexClassLoader(String apkPath) {
        return new DexClassLoader(apkPath,mContext.getDir("dex",Context.MODE_PRIVATE).getAbsolutePath(),null,mContext.getClassLoader());
    }

    public PluginApk getPluginApk() {
        return mPluginApk;
    }

    private static class Holder{
        private static PluginManager sPluginManager = new PluginManager();
    }

}
