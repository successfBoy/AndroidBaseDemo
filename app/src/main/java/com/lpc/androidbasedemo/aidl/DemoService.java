package com.lpc.androidbasedemo.aidl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/*
 * @author lipengcheng
 * create at  2019/5/19
 * description:
 */
public class DemoService extends Service {
    private IBinder mIBinder = new LocalBinder();
    public String mName = "lipengcheng";

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     * as given to {@link Context#bindService
     * Context.bindService}.  Note that any extras that were included with
     * the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    public class LocalBinder extends DemoStub {

        @Override
        public String getName() throws RemoteException {
            return mName;
        }

        @Override
        public void setName(String name) throws RemoteException {
            mName = name;
        }

        /**
         * Retrieve the Binder object associated with this interface.
         * You must use this instead of a plain cast, so that proxy objects
         * can return the correct result.
         */
        @Override
        public IBinder asBinder() {
            return null;
        }
    }
}
