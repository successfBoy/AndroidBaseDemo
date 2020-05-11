package com.lpc.androidbasedemo.scribble.service;

/**
 * Created by wangzhiyuan on 2017/4/18.
 * desc:
 */

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.ConnectCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.lpc.androidbasedemo.scribble.utils.ToastUtils;

import java.net.InetSocketAddress;

/**
 * Created by sunfusheng on 15/10/17.
 */
public class SocketClient implements Runnable {

    private String TAG = "log-" + this.getClass().getSimpleName();
    private AsyncSocket asyncSocket = null;
    private String host; //IP地址
    private int port; //端口
    private static SocketClient single = null;

    private final int ONE_SECOND = 1000;
    private final int TIME_CONNECT_FAILED_DELAY = 5;
    private final int TIME_CONNECT_SUCCESS_DELAY = 30;

    private int reConnectCount = 0;
    private boolean isConnected = false;

    private static final int CONNECT_SUCCESS = 0;
    private static final int CONNECT_FAILED = 1;

    private Handler mHandler = new Handler(Looper.getMainLooper(),new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    }) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONNECT_SUCCESS:
                    sendConnectInfo();
                    break;
                case CONNECT_FAILED:
                    closeSocket();
                    connect();
                    break;
            }
        }
    };

    //Socket 重连机制
    @Override
    public void run() {
        mHandler.postDelayed(this, ONE_SECOND);
        reConnectCount ++;
        if (isConnected) {
            if (reConnectCount > TIME_CONNECT_SUCCESS_DELAY) {
                reConnectCount = 0;
                sendMessage(CONNECT_SUCCESS);
            }
        } else {
            if (reConnectCount%TIME_CONNECT_FAILED_DELAY == 0) {
                sendMessage(CONNECT_FAILED);
            }
        }
    }

    private void sendMessage(int what) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        mHandler.sendMessage(msg);
    }

    //发送连接成功确认信息
    private void sendConnectInfo() {
      /*  SocketSimpleEntity entity = new SocketSimpleEntity();
        entity.inname = SocketResponseType.TYPE_CONNECT;
        entity.status = 200;
        entity.info = "onConnect OK ...";
        writeString(FastJsonUtil.createJson(entity));*/
    }

    //处理连接成功信息
    private void handleConnectSocket(String jsonStr) {
       /* try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String responseType = jsonObject.getString(SocketResponseType.TYPE_KEY);
            if (responseType.equals(SocketResponseType.TYPE_CONNECT)) {
                reConnectCount = 0;
                SocketSimpleEntity socketSimpleEntity = FastJsonUtil.parseJson(jsonStr, SocketSimpleEntity.class);
                if (socketSimpleEntity.status == 200) {
                    isConnected = true;
                } else {
                    isConnected = false;
                }
            }
        } catch (JSONException e) {}*/
    }

    private SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
        mHandler.postDelayed(this, ONE_SECOND);
    }

    public static SocketClient getInstance(String host, int port) {
        if (single == null) {
            single = new SocketClient(host,port);
        }
        return single;
    }

    /**
     * 连接 Socket
     */
    public void connect() {
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, final AsyncSocket socket) {
                if (ex != null) {
                    Log.e(TAG, "AsyncSocket Exception: " + ex.getMessage());
                    return;
                }
                if (socket == null) {
                    closeSocket();
                    connect();
                    return;
                }
                asyncSocket = socket;
                Log.d(TAG, "---> Connected");
                onConnectCompletedCallBack(socket);
                sendConnectInfo();
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
                if (bb == null) {
                    return;
                }
                String jsonStr = new String(bb.getAllByteArray());
                if (TextUtils.isEmpty(jsonStr)) {
                    return;
                }
                handleConnectSocket(jsonStr);
                if (onDataCallbackListener != null) {
                    onDataCallbackListener.onDataCallBack(jsonStr);
                }
                Log.d(TAG, "Receive : " + jsonStr);
            }
        });

        socket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                isConnected = false;
                if (ex != null) {
                    Log.e(TAG, "AsyncSocket Exception: " + ex.getMessage());
                    return;
                }
                if (onCloseCallbackListener != null) {
                    onCloseCallbackListener.onCloseCallBack();
                }
                Log.d(TAG, "---> Closed");
            }
        });

        socket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                isConnected = false;
                if (ex != null) {
                    Log.e(TAG, "AsyncSocket Exception: " + ex.getMessage());
                    return;
                }
                if (onEndCallbackListener != null) {
                    onEndCallbackListener.onEndCallBack();
                }
                Log.d(TAG, "---> Ended");
            }
        });
    }

    /**
     * 通过 Socket 发送 String
     */
    public boolean writeString(String str) {
        return writeString(null, str);
    }
    public boolean writeString(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            if (context != null) {
               ToastUtils.show(context, "发送的数据为null");
            }
            return false;
        }
        if (asyncSocket == null) {
            Log.e(TAG, "socket == null");
            return false;
        }
        if (!isConnected && context != null) {
            ToastUtils.show(context, "请稍后重试");
        }
        Log.d(TAG, "--------------------------------------------------------");
        Log.d(TAG, "Send: " + str);
        ByteBufferList bbl = new ByteBufferList();
//        bbl.add(StringUtil.getByteBuffer(str));
        asyncSocket.write(bbl);
        return isConnected;
    }

    /**
     * Close Socket
     */
    private void closeSocket() {
        if (asyncSocket == null) {
            Log.e(TAG, "socket == null");
            return ;
        }
        Log.d(TAG, "Close Socket");
        asyncSocket.close();
        asyncSocket = null;
    }

    /**
     * 关闭Socket后，释放资源
     */
    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
        }
        closeSocket();
        single = null;
    }

    private OnDataCallbackListener onDataCallbackListener; //Socket 接收数据监听器
    private OnCloseCallbackListener onCloseCallbackListener; //Socket Closed 监听器
    private OnEndCallbackListener onEndCallbackListener; //Socket End 监听器

    public interface OnDataCallbackListener {
        void onDataCallBack(String jsonStr);
    }

    public interface OnCloseCallbackListener {
        void onCloseCallBack();
    }

    public interface OnEndCallbackListener {
        void onEndCallBack();
    }

    public void setOnDataCallbackListener(OnDataCallbackListener onDataCallbackListener) {
        this.onDataCallbackListener = onDataCallbackListener;
    }

    public void setOnCloseCallbackListener(OnCloseCallbackListener onCloseCallbackListener) {
        this.onCloseCallbackListener = onCloseCallbackListener;
    }

    public void setOnEndCallbackListener(OnEndCallbackListener onEndCallbackListener) {
        this.onEndCallbackListener = onEndCallbackListener;
    }
}

