package com.sixfriend.shiyou.mylibrary.plugin;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/*
 * @author lipengcheng
 * create at  2019-06-17
 * description:
 */
public class PluginApk {
    public DexClassLoader mDexClassLoader;
    public AssetManager mAssetManager;
    public Resources mResources;
    public PackageInfo mPackageInfo;

    public PluginApk(DexClassLoader dexClassLoader, Resources resources, PackageInfo packageInfo) {
        this.mDexClassLoader = dexClassLoader;
        this.mResources = resources;
        this.mAssetManager = resources.getAssets();
        this.mPackageInfo = packageInfo;
    }
}
