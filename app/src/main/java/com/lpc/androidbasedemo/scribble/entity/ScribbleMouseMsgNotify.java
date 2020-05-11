package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.BrushCmd;

import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTFStrFormBytes;


/**
 * Created by wangzhiyuan on 2016/11/15.
 * desc:
 */

public class ScribbleMouseMsgNotify extends Message {
    public static final int ID = 10005;
    private static final String TAG = "ScribbleMouseMsgNotify";
    int m_targetUserID;
    String m_pageTypeId;
    BrushCmd m_command;
    BrushData brushData;
    CmdData cmdData;


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


    public ScribbleMouseMsgNotify() {
        m_cmdId = ID;
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
//        createAction(brushData.m_pen.type);
/*        Log.d(TAG, "read: ====>"
                + this + "\n"
                + "brushData===>" + brushData + "\n " +
                "cmdData====>>" + cmdData);*/

    }


    @Override
    public String toString() {
        return "ScribbleMouseMsgNotify{" +
                "m_targetUserID=" + m_targetUserID +
                ", m_pageTypeId='" + m_pageTypeId + '\'' +
                ", m_command=" + m_command +
                ", brushData=" + brushData +
                ", cmdData=" + cmdData +
                '}';
    }
}
