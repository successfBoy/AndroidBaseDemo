package com.lpc.androidbasedemo.scribble.entity;

/**
 * Created by wangzhiyuan on 2018/1/31.
 * desc:
 */

public class WCRSocketMessage {
    private int cmdId;
    private byte[] body;

    public WCRSocketMessage(int cmdId, byte[] body) {
        this.cmdId = cmdId;
        this.body = body;
    }

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
