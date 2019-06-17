package com.lpc.androidbasedemo.aidl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

/*
 * @author lipengcheng
 * create at  2019/5/19
 * description:
 */
public class MessengerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
    /**
     * mHandler
     */
    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Messenger replyTo = msg.replyTo;
//            replyTo.send();
        }
    };
    Messenger mMessenger = new Messenger(mHandler);
}
