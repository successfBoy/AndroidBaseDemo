package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

import static com.lpc.androidbasedemo.scribble.utils.Utils.intToBytes;

/**
 * Created by wangzhiyuan on 2016/12/21.
 * desc:
 */

public class ExitRoomMsg extends Message {
    public static final int ID = 22;
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public byte[] write() {
        byte[] m_roomId = new byte[0];//Charset.forName("UTF-8").encode(roomId).array(); // getUTF8Bytes(roomId.toCharArray());
        try {
            m_roomId = roomId.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        int totallen = 12+8+m_roomId.length;
        int cmdbuffLen = totallen - 12;
        return Utils.concatAll(intToBytes(totallen, ByteOrder.BIG_ENDIAN),
                intToBytes(ID, ByteOrder.BIG_ENDIAN),
                intToBytes(1, ByteOrder.BIG_ENDIAN),
                intToBytes(cmdbuffLen, ByteOrder.BIG_ENDIAN),
                intToBytes(m_roomId.length, ByteOrder.BIG_ENDIAN),
                m_roomId);
    }

    @Override
    public void read(ByteBufferList bb) {

    }
}
