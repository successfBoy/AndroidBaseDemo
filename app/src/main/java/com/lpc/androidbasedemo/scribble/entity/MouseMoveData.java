package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.StrokeType;
import com.lpc.androidbasedemo.scribble.utils.Utils;

import java.nio.ByteOrder;

import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTF16Bytes;
import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTFStrFormBytes;
import static com.lpc.androidbasedemo.scribble.utils.Utils.intToBytes;

/**
 * Created by wangzhiyuan on 2016/12/20.
 * desc:
 */

public class MouseMoveData extends Message {

    int x;
    int y;
    int width;
    int height;
    String userid;
    String usename;
    boolean isteacher;
    StrokeType stroketype;
    boolean show;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public boolean isteacher() {
        return isteacher;
    }

    public void setIsteacher(boolean isteacher) {
        this.isteacher = isteacher;
    }

    public StrokeType getStroketype() {
        return stroketype;
    }

    public void setStroketype(StrokeType stroketype) {
        this.stroketype = stroketype;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public byte[] write() {
        byte[] userIdBytes = getUTF16Bytes(userid.toCharArray());
        byte[] userNameBytes = getUTF16Bytes(usename.toCharArray());
        return Utils.concatAll(
                intToBytes(x, ByteOrder.BIG_ENDIAN),
                intToBytes(y, ByteOrder.BIG_ENDIAN),
                intToBytes(width, ByteOrder.BIG_ENDIAN),
                intToBytes(height, ByteOrder.BIG_ENDIAN),
                intToBytes(userIdBytes.length, ByteOrder.BIG_ENDIAN),
                userIdBytes,
                intToBytes(userNameBytes.length, ByteOrder.BIG_ENDIAN),
                userNameBytes,
                isteacher ? new byte[]{0x01} : new byte[]{0},
                intToBytes(stroketype.ordinal(), ByteOrder.BIG_ENDIAN),
                show ? new byte[]{0x01} : new byte[]{0}
        );
    }

    @Override
    public void read(ByteBufferList bb) {
        x = bb.getInt();
        y = bb.getInt();
        width = bb.getInt();
        height = bb.getInt();
        userid = getUTFStrFormBytes(bb.getBytes(bb.getInt()));
        int usernameLen = bb.getInt();
        if (usernameLen > 0) {
            usename = getUTFStrFormBytes(bb.getBytes(usernameLen));
        }
        isteacher = bb.get() != 0;
        int stroketypelen = bb.getInt();
        if (stroketypelen > 0 && StrokeType.values().length > stroketypelen) {
            stroketype = StrokeType.values()[stroketypelen];
        }
        show = bb.get() != 0;
    }

    @Override
    public String toString() {
        return "MouseMoveData{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", userid='" + userid + '\'' +
                ", usename='" + usename + '\'' +
                ", isteacher=" + isteacher +
                ", stroketype=" + stroketype +
                ", show=" + show +
                '}';
    }
}