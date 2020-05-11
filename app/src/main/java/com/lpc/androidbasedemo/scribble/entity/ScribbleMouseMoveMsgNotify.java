package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;

import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTFStrFormBytes;

/**
 * Created by wangzhiyuan on 2016/12/20.
 * desc:
 */

public class ScribbleMouseMoveMsgNotify extends Message {
    public static final int ID = 10012;

    int userid;
    String pageTypeId;
    MouseMoveData  data;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getPageTypeId() {
        return pageTypeId;
    }

    public void setPageTypeId(String pageTypeId) {
        this.pageTypeId = pageTypeId;
    }

    public MouseMoveData getData() {
        return data;
    }

    public void setData(MouseMoveData data) {
        this.data = data;
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
        userid = bb.getInt();
        int pageTypeIdLen = bb.getInt();
        if(pageTypeIdLen>0) {
            pageTypeId = getUTFStrFormBytes(bb.getBytes(pageTypeIdLen));
        }
        data = new MouseMoveData();
        data.read(bb);
    }

    @Override
    public String toString() {
        return "ScribbleMouseMoveMsgNotify{" +
                "userid=" + userid +
                ", pageTypeId='" + pageTypeId + '\'' +
                ", data=" + data +
                '}';
    }
}
