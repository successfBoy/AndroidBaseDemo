package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;

/**
 * Created by wangzhiyuan on 2016/12/29.
 * desc:
 */

public class EnterRoomMsgNotify extends Message {
    public static final int ID = 10021;
    int m_seatId;
    int m_userId;
    int m_role;

    public int getM_seatId() {
        return m_seatId;
    }

    public void setM_seatId(int m_seatId) {
        this.m_seatId = m_seatId;
    }

    public int getM_userId() {
        return m_userId;
    }

    public void setM_userId(int m_userId) {
        this.m_userId = m_userId;
    }

    public int getM_role() {
        return m_role;
    }

    public void setM_role(int m_role) {
        this.m_role = m_role;
    }

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
         m_seatId = bb.getInt();
         m_userId = bb.getInt();
         m_role = bb.getInt();
    }
}
