package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;

/**
 * Created by wangzhiyuan on 2016/11/15.
 * desc:
 */

public abstract class Message {
    protected int m_reqId;
    protected int m_packageLen;
    protected int m_cmdId;
    protected int m_cmdBufferLen;

    public int getM_cmdId() {
        return m_cmdId;
    }

    public void setM_cmdId(int m_cmdId) {
        this.m_cmdId = m_cmdId;
    }

    protected Action curAction;

    public Action getCurAction() {
        return curAction;
    }

    public abstract byte[] write();

    public abstract void read(ByteBufferList bb);
}
