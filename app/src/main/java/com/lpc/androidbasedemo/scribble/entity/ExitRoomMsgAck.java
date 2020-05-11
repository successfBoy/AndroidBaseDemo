package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;

/**
 * Created by wangzhiyuan on 2017/3/13.
 * desc:
 */

public class ExitRoomMsgAck extends Message{
    public static final int ID = 1002;

    public int getM_result() {
        return m_result;
    }

    public void setM_result(int m_result) {
        this.m_result = m_result;
    }

    int m_result;

    @Override
    public byte[] write() {
        return new byte[0];
    }

    @Override
    public void read(ByteBufferList bb) {
        m_packageLen = bb.getInt();
        m_cmdId = bb.getInt();
        m_reqId = bb.getInt();
        m_cmdBufferLen = bb.getInt();
        m_result = bb.getInt();
    }
}
