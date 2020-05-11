package com.lpc.androidbasedemo.scribble;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by wangzhiyuan on 2016/11/17.
 * desc:
 */

public class APPContex extends Application {
    private static final String TAG = "APPContex";
    private static APPContex sContext;

    public static APPContex getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

       /*
            // 针对线程的相关策略
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());

            // 针对VM的相关策略
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
 */

        sContext = this;
//        ScribbleManager.getsInstance().initSDK(this);
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
       /* Logger.init().logAdapter(
                      new MultiLogAdapter(Arrays.asList(
                      new AndroidLogAdapter(),  // standard android logcat
                      FileLogger.builder()      // file logger
                          .folderName("scribble")  // folder (starting from Environment.getExternalStorageDirectory())
                          .logLevel(android.util.Log.DEBUG) // only log INFO and up
                          .build())));
        Log.d(TAG, "onCreate: sdk init ");*/
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
    @Override
    protected void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
    }
}
