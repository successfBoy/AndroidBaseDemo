package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.Utils;

/**
 * Created by wangzhiyuan on 2017/4/6.
 * desc:
 */

public class ReStaticTuyaNotify extends Message {
    public static final int ID = 10013;
    private String docid;

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    private int pageid;
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
        int docidLen = bb.getInt();
        docid = Utils.getUTFStrFormBytes(bb.getBytes(docidLen));
        pageid = bb.getInt();


    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }
}
