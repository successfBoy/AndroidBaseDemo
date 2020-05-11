package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.Utils;

import java.nio.ByteOrder;

import static com.lpc.androidbasedemo.scribble.utils.Utils.intToBytes;

/**
 * Created by wangzhiyuan on 2016/12/19.
 * desc:
 */

public class S2C_RequestPatchedMouseMsgAck extends Message {
    public static final int ID = 3006;

    int m_srcUserID;
    int m_result;
    int m_totalMsgCnt;
    int m_msgBufferLen;
    String m_pageTypeId;
    int m_pageId;

    public int getM_srcUserID() {
        return m_srcUserID;
    }

    public void setM_srcUserID(int m_srcUserID) {
        this.m_srcUserID = m_srcUserID;
    }

    public int getM_result() {
        return m_result;
    }

    public void setM_result(int m_result) {
        this.m_result = m_result;
    }

    public int getM_totalMsgCnt() {
        return m_totalMsgCnt;
    }

    public void setM_totalMsgCnt(int m_totalMsgCnt) {
        this.m_totalMsgCnt = m_totalMsgCnt;
    }

    public int getM_msgBufferLen() {
        return m_msgBufferLen;
    }

    public void setM_msgBufferLen(int m_msgBufferLen) {
        this.m_msgBufferLen = m_msgBufferLen;
    }

    public String getM_pageTypeId() {
        return m_pageTypeId;
    }

    public void setM_pageTypeId(String m_pageTypeId) {
        this.m_pageTypeId = m_pageTypeId;
    }

    public int getM_pageId() {
        return m_pageId;
    }

    public void setM_pageId(int m_pageId) {
        this.m_pageId = m_pageId;
    }

    @Override
    public byte[] write() {
        int totallen = 12 + 12 ;
        int cmdbuffLen = totallen - 12;
        return Utils.concatAll(intToBytes(totallen, ByteOrder.BIG_ENDIAN),
                intToBytes(ID, ByteOrder.BIG_ENDIAN),
                intToBytes(1, ByteOrder.BIG_ENDIAN),
                intToBytes(cmdbuffLen, ByteOrder.BIG_ENDIAN),
                intToBytes(m_srcUserID, ByteOrder.BIG_ENDIAN),
                intToBytes(-1001, ByteOrder.BIG_ENDIAN)
        );
    }

    @Override
    public void read(ByteBufferList bb) {

    }
//    QByteArray m_data;

    @Override
    public String toString() {
        return "S2C_RequestPatchedMouseMsgAck{" +
                "m_srcUserID=" + m_srcUserID +
                ", m_result=" + m_result +
                ", m_totalMsgCnt=" + m_totalMsgCnt +
                ", m_msgBufferLen=" + m_msgBufferLen +
                ", m_pageTypeId='" + m_pageTypeId + '\'' +
                ", m_pageId=" + m_pageId +
                '}';
    }
}
