package com.lpc.androidbasedemo.activity;

import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.FacebookSdk;
import com.lpc.androidbasedemo.BuildConfig;
import com.lpc.androidbasedemo.common.tool.LogUtils;
import com.squareup.leakcanary.LeakCanary;

import java.util.Properties;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/29
 * description:
 */
public class BaseDemoApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();


        initARouter();
        initLogUtils();
        if (!FacebookSdk.isInitialized()) {

            FacebookSdk.sdkInitialize(getApplicationContext());
        }

        initLeakCanary();

        initNetProxy();

    }

    private void initARouter() {
        ARouter.init(this);
    }

    private void initNetProxy() {
        Properties prop = System.getProperties();
        //proxyhostIPaddress
        String proxyHost = "localhost";
        //proxyport
        String proxyPort = "8118";
        prop.put("proxyHost", proxyHost);
        prop.put("proxyPort", proxyPort);
        prop.put("proxySet", "true");

//在对相关的网络库进行初始化
//        OkGo.init(this);
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void initLogUtils() {
        LogUtils.Builder lBuilder = new LogUtils.Builder(getApplicationContext())
                .setLogSwitch(BuildConfig.DEBUG)// 设置log总开关，默认开
                .setGlobalTag("AndroidBaseDemo")// 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
                .setLogFilter(LogUtils.V);// log过滤器，和logcat过滤器同理，默认Verbose
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
}
