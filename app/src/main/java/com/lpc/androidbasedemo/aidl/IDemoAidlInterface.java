package com.lpc.androidbasedemo.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/*
 * @author lipengcheng
 * create at  2019/5/19
 * description:
 */
public interface IDemoAidlInterface extends IInterface {
    String DESCRIPTOR = "com.lpc.androidbasedemo.aidl.IDemoAidlInterface";

    int TRANSACTION_getName = IBinder.FIRST_CALL_TRANSACTION +0;
    int TRANSACTION_setName = IBinder.FIRST_CALL_TRANSACTION +1;

    String getName()throws RemoteException;
    void setName(String name)throws RemoteException;

}
