package com.lpc.androidbasedemo.scribble.utils;

import com.lpc.androidbasedemo.scribble.entity.EnterRoomMsgAck;
import com.lpc.androidbasedemo.scribble.entity.EnterRoomMsgNotify;
import com.lpc.androidbasedemo.scribble.entity.Message;
import com.lpc.androidbasedemo.scribble.entity.ReStaticTuyaNotify;
import com.lpc.androidbasedemo.scribble.entity.RequestPatchedMouseMsgAck;
import com.lpc.androidbasedemo.scribble.entity.S2C_RequestPatchedMouseMsg;
import com.lpc.androidbasedemo.scribble.entity.ScribbleMouseMoveMsgNotify;
import com.lpc.androidbasedemo.scribble.entity.ScribbleMouseMsgNotify;

/**
 * Created by wangzhiyuan on 2016/11/16.
 * desc:
 */

public class MessageGenerator {
    public static Message createMessageByCmdId(int cmdId) {
        switch (cmdId) {
            case ScribbleMouseMsgNotify.ID:
                return new ScribbleMouseMsgNotify();
            case EnterRoomMsgAck.ID:
                return new EnterRoomMsgAck();
            case RequestPatchedMouseMsgAck.ID:
                return new RequestPatchedMouseMsgAck();
            case S2C_RequestPatchedMouseMsg.ID:
                return new S2C_RequestPatchedMouseMsg();
            case ScribbleMouseMoveMsgNotify.ID:
            return new ScribbleMouseMoveMsgNotify();
            case EnterRoomMsgNotify.ID:
                return new EnterRoomMsgNotify();
            case ReStaticTuyaNotify.ID:
                return new ReStaticTuyaNotify();
            default:
                return null;
        }
    }
}
