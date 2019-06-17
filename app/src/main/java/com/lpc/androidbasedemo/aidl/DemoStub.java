package com.lpc.androidbasedemo.aidl;


import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/*
 * @author lipengcheng
 * create at  2019/5/19
 * description:
 */
public abstract class DemoStub extends Binder implements IDemoAidlInterface {
    public DemoStub(){
        this.attachInterface(this,DESCRIPTOR);
    }

    public static IDemoAidlInterface asInterface(IBinder obj){
        if(obj == null){
            return null;
        }

        IInterface in = obj.queryLocalInterface(DESCRIPTOR);
        if(in != null && in instanceof IDemoAidlInterface){
            return (IDemoAidlInterface) in;
        }
        return new DemoStub.Proxy(obj);
    }

//    @Override
//    public String getName() throws RemoteException{
//        return null;
//    }
//
//    @Override
//    public void setName(String name) throws RemoteException{
//
//    }
//
//    /**
//     * Retrieve the Binder object associated with this interface.
//     * You must use this instead of a plain cast, so that proxy objects
//     * can return the correct result.
//     */
//    @Override
//    public IBinder asBinder() {
//        return this;
//    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {

        switch (code){
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case TRANSACTION_setName:
                data.enforceInterface(DESCRIPTOR);
                String name = data.readString();
                setName(name);
                reply.writeNoException();
                return true;
            case TRANSACTION_getName:
                reply.writeString(getName());
                reply.writeNoException();
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IDemoAidlInterface{
        private IBinder mRemote;
        public Proxy(IBinder remote){
            mRemote = remote;
        }

        /**
         * Retrieve the Binder object associated with this interface.
         * You must use this instead of a plain cast, so that proxy objects
         * can return the correct result.
         */
        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor(){
            return DESCRIPTOR;
        }


        @Override
        public String getName() throws RemoteException{
            Parcel data = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            String name;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getName,data,replay,0);
                replay.writeNoException();
                name = replay.readString();
            }finally {
                data.recycle();
                replay.recycle();;
            }

            return name;
        }

        @Override
        public void setName(String name) throws RemoteException{
            Parcel data = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);

                if(TextUtils.isEmpty(name)){
                    data.writeInt(1);
                    data.writeString(name);

                }else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_setName,data,replay,0);
                replay.readException();
            }finally {
                data.recycle();
                replay.recycle();
            }
        }
    }
}
