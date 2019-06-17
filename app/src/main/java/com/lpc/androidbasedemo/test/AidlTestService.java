package com.lpc.androidbasedemo.test;

import android.os.RemoteException;

import com.lpc.androidbasedemo.aidl.IMyAidlInterface;

/*
 * @author lipengcheng
 * create at  2019/1/7
 * description:
 */
public class AidlTestService extends IMyAidlInterface.Stub {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     *
     * @param anInt
     * @param aLong
     * @param aBoolean
     * @param aFloat
     * @param aDouble
     * @param aString
     */
    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public int getAge() throws RemoteException {
        return 0;
    }
}
