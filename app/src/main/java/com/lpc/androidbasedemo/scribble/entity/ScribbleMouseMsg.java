package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.BrushCmd;
import com.lpc.androidbasedemo.scribble.utils.Utils;

import java.nio.ByteOrder;

import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTF16Bytes;
import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTFStrFormBytes;
import static com.lpc.androidbasedemo.scribble.utils.Utils.intToBytes;

/**
 * Created by wangzhiyuan on 2017/8/2.
 * desc:
 */

public class ScribbleMouseMsg extends Message {
    public static final  int ID = 5;

    private static final String TAG = "ScribbleMouseMsg";
    int m_targetUserID;
    String m_pageTypeId;
    BrushCmd m_command;
    BrushData brushData;
    CmdData cmdData;

    public int getReq_Id() {
        return req_Id;
    }

    public void setReq_Id(int req_Id) {
        this.req_Id = req_Id;
    }

    int req_Id;

    public static String getTAG() {
        return TAG;
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

    public BrushCmd getM_command() {
        return m_command;
    }

    public void setM_command(BrushCmd m_command) {
        this.m_command = m_command;
    }

    public CmdData getCmdData() {
        return cmdData;
    }

    public void setCmdData(CmdData cmdData) {
        this.cmdData = cmdData;
    }

    public BrushData getBrushData() {
        return brushData;
    }

    public void setBrushData(BrushData brushData) {
        this.brushData = brushData;
    }

    @Override
    public byte[] write() {
        byte[] pageTypeId = new byte[0];

        pageTypeId = getUTF16Bytes(m_pageTypeId.toCharArray());

        byte[] data = (m_command == BrushCmd.CMD_SUBBLE? brushData.write():cmdData.write());
        m_packageLen = 12 + 16 + pageTypeId.length + data.length;
        m_cmdBufferLen = m_packageLen - 12;
        return Utils.concatAll(
                intToBytes(m_packageLen, ByteOrder.BIG_ENDIAN),//报文长度
                intToBytes(ID, ByteOrder.BIG_ENDIAN),//协议命令
                intToBytes(req_Id, ByteOrder.BIG_ENDIAN),//序列号
                intToBytes(m_cmdBufferLen, ByteOrder.BIG_ENDIAN),//协议数据长度
                intToBytes(m_targetUserID, ByteOrder.BIG_ENDIAN),//userid
                intToBytes(m_command.ordinal(), ByteOrder.BIG_ENDIAN),//涂鸦命令
                intToBytes(pageTypeId.length, ByteOrder.BIG_ENDIAN),//pagetypeid
                pageTypeId,//pagetypeid
                data);
    }

    @Override
    public void read(ByteBufferList bb) {
        m_packageLen = bb.getInt();
        m_cmdId = bb.getInt();
        m_reqId = bb.getInt();
        m_cmdBufferLen = bb.getInt();
        m_targetUserID = bb.getInt();
        m_command = BrushCmd.values()[bb.getInt()];

        m_pageTypeId = getUTFStrFormBytes(bb.getBytes(bb.getInt()));
        if (m_command == BrushCmd.CMD_SUBBLE) {
            brushData = new BrushData();
            brushData.read(bb);
        } else {
            cmdData = new CmdData();
            cmdData.setM_cmd(m_command);
            cmdData.read(bb);
        }
    }
}
