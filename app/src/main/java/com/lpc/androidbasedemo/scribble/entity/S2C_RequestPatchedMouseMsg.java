package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;

import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTFStrFormBytes;

/**
 * Created by wangzhiyuan on 2016/12/19.
 * desc:
 */

public class S2C_RequestPatchedMouseMsg extends Message {
  public   static final  int ID = 2006;
    String m_pageTypeId;
    int m_srcUserID;

    public String getM_pageTypeId() {
        return m_pageTypeId;
    }

    public void setM_pageTypeId(String m_pageTypeId) {
        this.m_pageTypeId = m_pageTypeId;
    }

    public int getM_srcUserID() {
        return m_srcUserID;
    }

    public void setM_srcUserID(int m_srcUserID) {
        this.m_srcUserID = m_srcUserID;
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
        m_srcUserID = bb.getInt();
        m_pageTypeId = getUTFStrFormBytes(bb.getBytes(bb.getInt()));

    }

    @Override
    public String toString() {
        return "S2C_RequestPatchedMouseMsg{" +
                "m_pageTypeId='" + m_pageTypeId + '\'' +
                ", m_srcUserID=" + m_srcUserID +
                '}';
    }
}
