package com.lpc.androidbasedemo.scribble.entity;

import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.utils.Utils;

import java.nio.ByteOrder;

/**
 * Created by liuliwan on 2018/6/4.
 */

public class ScribbleClearMsg extends Message {
    public static final int ID = 10;
    private int cmdID = 912918;
    private int targetSentID = 0;

    @Override
    public byte[] write() {
        int totallen = 12+8;
        int cmdBuffLen = totallen - 12;
        return Utils.concatAll(Utils.intToBytes(totallen, ByteOrder.BIG_ENDIAN),
                Utils.intToBytes(cmdID, ByteOrder.BIG_ENDIAN),
                Utils.intToBytes(0, ByteOrder.BIG_ENDIAN),
                Utils.intToBytes(cmdBuffLen, ByteOrder.BIG_ENDIAN),
                Utils.intToBytes(targetSentID, ByteOrder.BIG_ENDIAN)
                );

    }

    @Override
    public void read(ByteBufferList bb) {
    }
}
