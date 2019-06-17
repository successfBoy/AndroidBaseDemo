package com.lpc.androidbasedemo.activity;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/*
 * @author lipengcheng
 * create at  2018/12/30
 * description:
 */
public class BindTestService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new LocatBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    public class LocatBinder extends Binder{
        BindTestService getService(){
            return BindTestService.this;
        };
    }

    public int getCount(){
        return  0;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
        startForeground(1,new Notification());
//        stopForeground(1);
//        startActivity();
        test(this);

    }
    public static void test(Service context){
        context.startActivity(new Intent());
    }
}
