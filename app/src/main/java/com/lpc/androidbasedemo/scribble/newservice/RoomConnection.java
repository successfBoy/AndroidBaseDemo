package com.lpc.androidbasedemo.scribble.newservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.lpc.androidbasedemo.scribble.entity.IpAddress;
import com.orhanobut.logger.Logger;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wangzhiyuan
 * @date 2016/11/14
 * desc:
 */
public class RoomConnection implements Runnable {
    /**
     * 隔N秒后重连
     */
    private static final int RE_CONN_WAIT_SECONDS = 0;
    /**
     * 多长时间为请求后，发送心跳
     */
    private static final int WRITE_WAIT_SECONDS = 3;
    private volatile String mHost;
    private volatile int mPort;
    private AsyncSocket mSocket;
    private static final String TAG = "RoomConnection";
    private int waitCounts = 0;

    private ScheduledExecutorService executorService;
    private AsyncSocketListener asyncSocketListener;
    /**
     * IP列表
     */
    private List<IpAddress> mIpList;
    private int ipIndex = 0;

    public RoomConnection() {
        executorService = Executors.newScheduledThreadPool(5);
    }

    private void disableConnectionReuseIfNecessary() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    public void socketConnect() {
        Logger.d("Socket is connecting");
        if (mIpList != null && mIpList.size() > 0) {
            mHost = mIpList.get(ipIndex).getIp();
            mPort = mIpList.get(ipIndex).getPort();
            ipIndex = (ipIndex + 1) % mIpList.size();
        }
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(mHost, mPort), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                if (ex != null) {
                    Logger.e("AsyncSocket Exception: " + ex.getMessage());
                    return;
                }
                if (socket == null) {
                    Logger.e("AsyncSocket Exception: " + "socket == null");

                    closeSocket();
                    socketConnect();
                    return;
                }
                mSocket = socket;
                onConnectCompletedCallBack(mSocket);
                asyncSocketListener.onConnected(mSocket, mHost, mPort);
            }
        });
    }

    /**
     * 连接成功后回调
     */
    public void onConnectCompletedCallBack(final AsyncSocket socket) {
        socket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                asyncSocketListener.onDataCallBack(bb);
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {

                if (ex != null) {
                    ex.printStackTrace();
                    Logger.e("AsyncSocket Exception: %s", ex.getMessage());
                    return;
                }
                asyncSocketListener.onCloseCallBack();
                Logger.d("[Client] Successfully closed connection");
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {

                if (ex != null) {
                    ex.printStackTrace();
                    Logger.e("AsyncSocket Exception: %s", ex.getMessage());
                    return;
                }
                asyncSocketListener.onEndCallBack();

                Logger.d("[Client] Successfully end connection");
            }
        });
    }

    public void connect(final String host, final int port) {
        this.mHost = host;
        this.mPort = port;
        if (executorService.isShutdown()) {
            executorService = Executors.newScheduledThreadPool(5);
        }
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    waitCounts++;
                    boolean netstatus = getNetState();

                    if (!socketIsConnect() && netstatus) {
                        Logger.d("socket is reConnecting: netstatus:" + netstatus + "mHost:" + mHost + "mPort:" + mPort);
                        socketConnect();
                    } else {
                        if (9 == waitCounts) {
                            asyncSocketListener.onHeartBagSend();
                            waitCounts = 0;
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, RE_CONN_WAIT_SECONDS, WRITE_WAIT_SECONDS, TimeUnit.SECONDS);
    }

    public boolean socketIsConnect() {
        return mSocket != null && mSocket.isOpen();
    }

    private boolean getNetState() {
        ConnectivityManager cm =
                (ConnectivityManager) ScribbleManager.getsInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }

    public void setIpAddressList(List<IpAddress> list) {
        this.mIpList = list;
    }

    /**
     * Close Socket
     */
    private void closeSocket() {
        if (mSocket == null) {
            Log.e(TAG, "socket == null");
            return;
        }
        Logger.d(TAG + " %s", "Close Socket");
        mSocket.close();
    }

    public void release() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(500, TimeUnit.MICROSECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                executorService.shutdownNow();
            }
        }
        closeSocket();
    }

    @Override
    public void run() {

    }

    public interface AsyncSocketListener {
        void onConnected(AsyncSocket socket, String host, int port);

        void onDataCallBack(ByteBufferList bb);

        void onEndCallBack();

        void onCloseCallBack();

        void onHeartBagSend();
    }

    public void setAsyncSocketistener(AsyncSocketListener asyncSocketListener) {
        this.asyncSocketListener = asyncSocketListener;
    }
}
