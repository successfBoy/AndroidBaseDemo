package com.lpc.androidbasedemo.scribble.utils;

/**
 * Created by wangzhiyuan on 2016/12/8.
 * desc:
 */

public interface ScribbleInteractiveListener {
    /**
     * TCP连接状态
     * @param status true = success
     */
    void onConnectStatus(boolean status, Exception ex);
    /**
     * 进房成功与失败回调
     * @param status true = success
     */
    void onEnterRoomStatus(boolean status);

    /**
     * 退房成功与失败回调
     * @param status true = success
     */
    void onExitRoomStatus(boolean status);
}
