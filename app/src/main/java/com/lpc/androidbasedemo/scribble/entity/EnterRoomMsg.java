package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

import static com.lpc.androidbasedemo.scribble.utils.Utils.intToBytes;


/**
 * Created by wangzhiyuan on 2016/11/14.
 * desc:
 */

public class EnterRoomMsg extends Message {
    public static final int ID = 21;

    private String roomId;
    private int role;
    private int roomType;
    private int userId;

    @Override
    public byte[] write() {
        byte[] m_roomId = new byte[0];//Charset.forName("UTF-8").encode(roomId).array(); // getUTF8Bytes(roomId.toCharArray());
        try {
            m_roomId = roomId.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int totallen = 12 + 20 + m_roomId.length;
        int cmdbuffLen = totallen - 12;

        return Utils.concatAll(intToBytes(totallen, ByteOrder.BIG_ENDIAN),
        intToBytes(ID, ByteOrder.BIG_ENDIAN),
        intToBytes(1, ByteOrder.BIG_ENDIAN),
        intToBytes(cmdbuffLen, ByteOrder.BIG_ENDIAN),
        intToBytes(m_roomId.length, ByteOrder.BIG_ENDIAN),
        m_roomId,
        intToBytes(role, ByteOrder.BIG_ENDIAN),
        intToBytes(roomType, ByteOrder.BIG_ENDIAN),
        intToBytes(userId, ByteOrder.BIG_ENDIAN)
        );
 /*       ByteBuffer b = ByteBuffer.allocate(32);
        b.putInt(32);//报文长度
        b.putInt(ID);//协议命令
        b.putInt(1);//序列号
        b.putInt(20);//协议长度
        b.putInt(roomId);
        b.putInt(role);
        b.putInt(roomType);
        b.putInt(userId);
        return b.array();*/
    }

    @Override
    public void read(ByteBufferList bb) {

    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "EnterRoomMsg{" +
                "roomId=" + roomId +
                ", role=" + role +
                ", roomType=" + roomType +
                ", userId=" + userId +
                '}';
    }
}
