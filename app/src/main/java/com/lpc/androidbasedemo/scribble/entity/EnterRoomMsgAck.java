package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.Permission;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzhiyuan on 2016/11/22.
 * desc:
 */

public class EnterRoomMsgAck extends Message {
    public static final int ID = 1021;
    int m_enterResult; // 0 表示成功，其他表示失败
    int m_extBufferLen;
    int m_seatId;
    int m_permission;
    int m_userCount;
    Map<Integer, Permission> m_user_premi = new HashMap<>();

    public static int getID() {
        return ID;
    }

    public int getM_enterResult() {
        return m_enterResult;
    }

    public void setM_enterResult(int m_enterResult) {
        this.m_enterResult = m_enterResult;
    }

    public int getM_extBufferLen() {
        return m_extBufferLen;
    }

    public void setM_extBufferLen(int m_extBufferLen) {
        this.m_extBufferLen = m_extBufferLen;
    }

    public int getM_seatId() {
        return m_seatId;
    }

    public void setM_seatId(int m_seatId) {
        this.m_seatId = m_seatId;
    }

    public int getM_permission() {
        return m_permission;
    }

    public void setM_permission(int m_permission) {
        this.m_permission = m_permission;
    }

    public int getM_userCount() {
        return m_userCount;
    }

    public void setM_userCount(int m_userCount) {
        this.m_userCount = m_userCount;
    }

    public Map<Integer, Permission> getM_user_premi() {
        return m_user_premi;
    }

    public void setM_user_premi(Map<Integer, Permission> m_user_premi) {
        this.m_user_premi = m_user_premi;
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
        m_enterResult = bb.getInt();
        m_extBufferLen = bb.getInt();
        m_seatId = bb.getInt();
        m_permission = bb.getInt();
        m_userCount = bb.getInt();
        for (int i = 0; i < m_userCount; i++) {
            m_user_premi.put(bb.getInt(), Permission.values()[bb.getInt() - 1]);
        }
    }

    @Override
    public String toString() {
        return "EnterRoomMsgAck{" +
                "m_enterResult=" + m_enterResult +
                ", m_extBufferLen=" + m_extBufferLen +
                ", m_seatId=" + m_seatId +
                ", m_permission=" + m_permission +
                ", m_userCount=" + m_userCount +
                ", m_user_premi=" + m_user_premi +
                '}';
    }
}
