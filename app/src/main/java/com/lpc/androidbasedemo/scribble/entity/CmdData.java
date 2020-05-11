package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.BrushCmd;
import com.lpc.androidbasedemo.scribble.utils.Utils;
import com.lpc.androidbasedemo.scribble.utils.eBrushDataShowFlag;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.lpc.androidbasedemo.scribble.utils.Utils.intToBytes;

/**
 * Created by wangzhiyuan on 2016/11/16.
 * desc:
 */

public class CmdData extends Message {
    int m_userId;

    /*切换涂鸦区域相关*/
    String m_pageTypeId;

    /*移动，缩放相关数据删除相关*/
    int m_pageId;

    BrushCmd m_cmd;

    //bool m_enable;

    public int getM_pageId() {
        return m_pageId;
    }

    public void setM_pageId(int m_pageId) {
        this.m_pageId = m_pageId;
    }

    public int getM_userId() {
        return m_userId;
    }

    public void setM_userId(int m_userId) {
        this.m_userId = m_userId;
    }

    public String getM_pageTypeId() {
        return m_pageTypeId;
    }

    public void setM_pageTypeId(String m_pageTypeId) {
        this.m_pageTypeId = m_pageTypeId;
    }

    public BrushCmd getM_cmd() {
        return m_cmd;
    }

    public void setM_cmd(BrushCmd m_cmd) {
        this.m_cmd = m_cmd;
    }

    byte m_showflag;

    List<Integer> m_strokeIds = new ArrayList<>();//只给删除用
    List<BrushData> m_brushDatas = new ArrayList<>();//给移动和缩放用

    public List<Integer> getM_strokeIds() {
        return m_strokeIds;
    }

    public byte getM_showflag() {
        return m_showflag;
    }

    public boolean getM_showFlag(eBrushDataShowFlag showFlag) {
        return (m_showflag & showFlag.getIndex()) != 0;
    }


    public void setM_showflag(byte m_showflag) {
        this.m_showflag = m_showflag;
    }

    public void setM_strokeIds(List<Integer> m_strokeIds) {
        this.m_strokeIds = m_strokeIds;
    }

    public List<BrushData> getM_brushDatas() {
        return m_brushDatas;
    }

    public void setM_brushDatas(List<BrushData> m_brushDatas) {
        this.m_brushDatas = m_brushDatas;
    }

    @Override
    public byte[] write() {
        byte[] strokeidByte = new byte[0];
        Iterator<Integer> strokeidIterator = m_strokeIds.iterator();
        while(strokeidIterator.hasNext()){
            strokeidByte = Utils.concatAll(strokeidByte, intToBytes(strokeidIterator.next(), ByteOrder.BIG_ENDIAN));
        }
        byte[] brushDataByte = new byte[0];
        Iterator<BrushData> brushDataIterator = m_brushDatas.iterator();
        while(brushDataIterator.hasNext()){
            brushDataByte = Utils.concatAll(brushDataByte, brushDataIterator.next().write());
        }
        return Utils.concatAll(intToBytes(m_pageId, ByteOrder.BIG_ENDIAN),
                intToBytes(m_strokeIds.size(), ByteOrder.BIG_ENDIAN),
                strokeidByte,
                new byte[]{m_showflag},
                intToBytes(m_brushDatas.size(), ByteOrder.BIG_ENDIAN),
                brushDataByte
        );
    }

    @Override
    public void read(ByteBufferList bb) {
        m_pageId = bb.getInt();
        int listCnt = bb.getInt();
        for (int i = 0; i < listCnt; i++) {
            m_strokeIds.add(bb.getInt());
        }
        m_showflag = bb.get();
        listCnt = bb.getInt();
        for (int i = 0; i < listCnt; i++) {
            BrushData brushData = new BrushData();
            brushData.read(bb);
            m_brushDatas.add(brushData);
        }
    }

    @Override
    public String toString() {
        return "CmdData{" +
                "m_userId=" + m_userId +
                ", m_pageTypeId='" + m_pageTypeId + '\'' +
                ", m_pageId=" + m_pageId +
                ", m_cmd=" + m_cmd +
                ", m_showflag=" + m_showflag +
                ", m_strokeIds=" + m_strokeIds +
                ", m_brushDatas=" + m_brushDatas +
                '}';
    }
}
