package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.Utils;

import java.nio.ByteOrder;

import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTF16Bytes;
import static com.lpc.androidbasedemo.scribble.utils.Utils.intToBytes;

/**
 * @author wangyh
 * @date 2019-09-18
 * 发送激光笔消息
 */
public class LaserMouseMsg extends Message{
    public static final int ID = 12;

    int req_Id;
    int m_targetUserID;
    String m_pageTypeId;
    MouseMoveData mouseMoveData;

    public int getReq_Id() {
        return req_Id;
    }

    public void setReq_Id(int req_Id) {
        this.req_Id = req_Id;
    }

    public int getM_targetUserID() {
        return m_targetUserID;
    }

    public void setM_targetUserID(int m_targetUserID) {
        this.m_targetUserID = m_targetUserID;
    }

    public String getM_pageTypeId() {
        return m_pageTypeId;
    }

    public void setM_pageTypeId(String m_pageTypeId) {
        this.m_pageTypeId = m_pageTypeId;
    }

    public MouseMoveData getMouseMoveData() {
        return mouseMoveData;
    }

    public void setMouseMoveData(MouseMoveData mouseMoveData) {
        this.mouseMoveData = mouseMoveData;
    }

    @Override
    public byte[] write() {
        byte[] pageTypeId = getUTF16Bytes(m_pageTypeId.toCharArray());
        byte[] data = mouseMoveData.write();
        m_packageLen = 12 + 12 + pageTypeId.length + data.length;
        m_cmdBufferLen = m_packageLen - 12;
        return Utils.concatAll(
                intToBytes(m_packageLen, ByteOrder.BIG_ENDIAN),//报文长度
                intToBytes(12, ByteOrder.BIG_ENDIAN),//协议命令
                intToBytes(req_Id, ByteOrder.BIG_ENDIAN),//序列号
                intToBytes(m_cmdBufferLen, ByteOrder.BIG_ENDIAN),//协议数据长度
                intToBytes(m_targetUserID, ByteOrder.BIG_ENDIAN),//userid
                intToBytes(pageTypeId.length, ByteOrder.BIG_ENDIAN),//pagetypeid
                pageTypeId,//pagetypeid
                data);
    }

    @Override
    public void read(ByteBufferList bb) {

    }
}