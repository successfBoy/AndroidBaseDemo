package com.lpc.androidbasedemo.activity;

import android.app.Application;

import com.lpc.androidbasedemo.BuildConfig;
import com.lpc.androidbasedemo.common.tool.LogUtils;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/29
 * description:
 */
public class BaseDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLogUtils();
    }

    private void initLogUtils() {
        LogUtils.Builder lBuilder  = new LogUtils.Builder(getApplicationContext())
            .setLogSwitch(BuildConfig.DEBUG)// 设置log总开关，默认开
        .setGlobalTag("AndroidBaseDemo")// 设置log全局标签，默认为空
    // 当全局标签不为空时，我们输出的log全部为该tag，
    // 为空时，如果传入的tag为空那就显示类名，否则显示tag
        .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
        .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
        .setLogFilter(LogUtils.V);// log过滤器，和logcat过滤器同理，默认Verbose
    }
}
