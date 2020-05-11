package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;

import java.util.ArrayList;
import java.util.List;

import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTFStrFormBytes;

/**
 * Created by wangzhiyuan on 2016/11/22.
 * desc:接收静态涂鸦
 */

public class RequestPatchedMouseMsgAck extends Message {
    public static final int ID = 1006;
    int m_srcUserID;
    int m_result;
    int m_totalMsgCnt;
    int m_msgBufferLen;
    String m_pageTypeId;
    int m_pageId;
    List<Message> m_messages;
    private int listSize;
    List<BrushData> brushDataList = new ArrayList<>();

    public boolean isBurshIsComplete() {
        return burshIsComplete;
    }

    public void setBurshIsComplete(boolean burshIsComplete) {
        this.burshIsComplete = burshIsComplete;
    }

    public boolean burshIsComplete;

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

    public List<Message> getM_messages() {
        return m_messages;
    }

    public void setM_messages(List<Message> m_messages) {
        this.m_messages = m_messages;
    }

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public List<BrushData> getBrushDataList() {
        return brushDataList;
    }

    public void setBrushDataList(List<BrushData> brushDataList) {
        this.brushDataList = brushDataList;
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
        m_result = bb.getInt();
        if (m_result != 0)//TODO:其它状态需要另行处理
            return;

        m_totalMsgCnt = bb.getInt();
        if(m_totalMsgCnt==0){
            return;
        }
        m_msgBufferLen = bb.getInt();

        int _pageTypeLength = bb.getInt();
        m_pageTypeId = getUTFStrFormBytes(bb.getBytes(_pageTypeLength));
        m_pageId = bb.getInt();
        int _lastDataLength =  bb.getInt();//_lastDataLength
        int dct = bb.getInt();//跳过
        for (int i = 0; i < dct; i++) {
            BrushData brushData = new BrushData();
            brushData.read(bb);
            brushDataList.add(brushData);

        }


    }

    @Override
    public String toString() {
        return "RequestPatchedMouseMsgAck{" +
                "m_srcUserID=" + m_srcUserID +
                ", m_result=" + m_result +
                ", m_totalMsgCnt=" + m_totalMsgCnt +
                ", m_msgBufferLen=" + m_msgBufferLen +
                ", m_pageTypeId='" + m_pageTypeId + '\'' +
                ", m_pageId=" + m_pageId +
                ", m_messages=" + m_messages +
                ", listSize=" + listSize +
                ", brushDataList=" + brushDataList.size()+
                '}';
    }
}
