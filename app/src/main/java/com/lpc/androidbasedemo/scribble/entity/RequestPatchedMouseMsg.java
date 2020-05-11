package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.Utils;

import java.nio.ByteOrder;

import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTF16Bytes;
import static com.lpc.androidbasedemo.scribble.utils.Utils.intToBytes;


/**
 * Created by wangzhiyuan on 2016/11/22.
 * desc:
 */

public class RequestPatchedMouseMsg extends Message {
    public static final int ID = 6;
    String m_pageTypeId;
    int m_dstUserID;
    int pageid;

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public String getM_pageTypeId() {
        return m_pageTypeId;
    }

    public void setM_pageTypeId(String m_pageTypeId) {
        this.m_pageTypeId = m_pageTypeId;
    }

    public int getM_dstUserID() {
        return m_dstUserID;
    }

    public void setM_dstUserID(int m_dstUserID) {
        this.m_dstUserID = m_dstUserID;
    }

    @Override
    public byte[] write() {
        byte[] pageTypeId = getUTF16Bytes(m_pageTypeId.toCharArray());
        int totallen = 12 + 16+ pageTypeId.length;
        int cmdbuffLen = totallen - 12;
        return Utils.concatAll(intToBytes(totallen, ByteOrder.BIG_ENDIAN),
                intToBytes(6, ByteOrder.BIG_ENDIAN),
                intToBytes(1, ByteOrder.BIG_ENDIAN),
                intToBytes(cmdbuffLen, ByteOrder.BIG_ENDIAN),
                intToBytes(m_dstUserID, ByteOrder.BIG_ENDIAN),
                intToBytes(pageTypeId.length, ByteOrder.BIG_ENDIAN),
                pageTypeId,
                intToBytes(pageid, ByteOrder.BIG_ENDIAN)
        );
    }

    @Override
    public void read(ByteBufferList bb) {

    }

    @Override
    public String toString() {
        return "RequestPatchedMouseMsg{" +
                "m_pageTypeId='" + m_pageTypeId + '\'' +
                ", m_dstUserID=" + m_dstUserID +
                ", pageid=" + pageid +
                '}';
    }
}
