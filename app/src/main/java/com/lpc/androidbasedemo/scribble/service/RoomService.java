package com.lpc.androidbasedemo.scribble.service;

import android.text.TextUtils;
import android.util.Log;

import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.lpc.androidbasedemo.scribble.entity.EnterRoomMsg;
import com.lpc.androidbasedemo.scribble.entity.EnterRoomMsgAck;
import com.lpc.androidbasedemo.scribble.entity.EnterRoomMsgNotify;
import com.lpc.androidbasedemo.scribble.entity.ExitRoomMsg;
import com.lpc.androidbasedemo.scribble.entity.ExitRoomMsgAck;
import com.lpc.androidbasedemo.scribble.entity.Message;
import com.lpc.androidbasedemo.scribble.entity.ReStaticTuyaNotify;
import com.lpc.androidbasedemo.scribble.entity.RequestPatchedMouseMsg;
import com.lpc.androidbasedemo.scribble.entity.RequestPatchedMouseMsgAck;
import com.lpc.androidbasedemo.scribble.entity.S2C_RequestPatchedMouseMsg;
import com.lpc.androidbasedemo.scribble.entity.S2C_RequestPatchedMouseMsgAck;
import com.lpc.androidbasedemo.scribble.entity.ScribbleClearMsg;
import com.lpc.androidbasedemo.scribble.entity.ScribbleMouseMoveMsgNotify;
import com.lpc.androidbasedemo.scribble.entity.ScribbleMouseMsgNotify;
import com.lpc.androidbasedemo.scribble.newservice.PageManager;
import com.lpc.androidbasedemo.scribble.newservice.RoomConnection;
import com.lpc.androidbasedemo.scribble.newservice.ScribbleManager;
import com.lpc.androidbasedemo.scribble.utils.MessageGenerator;
import com.lpc.androidbasedemo.scribble.utils.Permission;
import com.lpc.androidbasedemo.scribble.utils.RoleType;
import com.lpc.androidbasedemo.scribble.utils.RoomState;
import com.lpc.androidbasedemo.scribble.utils.ScribbleInteractiveListener;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.lpc.androidbasedemo.scribble.service.RoomService.ReadState.STATE_READBODY;
import static com.lpc.androidbasedemo.scribble.service.RoomService.ReadState.STATE_READHEADER;

/**
 * @author wangzhiyuan
 * @date 2016/11/14
 * desc:
 */

public class RoomService implements RoomConnection.AsyncSocketListener {

    private static final String TAG = "RoomService";
    private static final int PACKAGE_HEADER_LEN = 4;
    private int m_pendingPackageTotalLen;
    private RoomConnection roomConnection;
    private ByteArrayOutputStream m_pendingPackageData;
    private ReadState m_readState = STATE_READHEADER;
    private String roomid;
    private Map<Integer, RoleType> pm;
    private PageManager pageManager;
    private RequestPatchedMouseMsgAck requestPatchedMouseMsgAckCache = new RequestPatchedMouseMsgAck();
    private int pageid;
    private String docid;
    private AsyncSocket socket;
    private ScribbleInteractiveListener scribbleInteractiveListener;
    private RoomState roomState;
    private int role;
    private int roomType;
    private int userId;

    enum ReadState {
        STATE_READHEADER,
        STATE_READBODY
    }

    public RoomService() {
        roomConnection = new RoomConnection();
        roomConnection.setAsyncSocketistener(this);
        pm = new HashMap<>();
    }

    public void sendData(final Message message) {
        Logger.d("[Client] old start sendData() %s", "--" + Arrays.toString(message.write()));
        if (socket == null) {
            return;
        }
        Util.writeAll(socket, message.write(), new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    Logger.e(ex.getMessage());
                }
                Logger.d("[Client] Successfully wrote message %s", message);
            }
        });
    }

    public void setScribbleInteractiveListener(ScribbleInteractiveListener scribbleInteractiveListener) {
        this.scribbleInteractiveListener = scribbleInteractiveListener;
    }

    public void connect(String host, int port, String roomid, int role, int roomType, int userId) {
        this.roomid = roomid;
        this.role = role;
        this.roomType = roomType;
        this.userId = userId;
        roomConnection.connect(host, port);
    }

    public void exitRoom() {
        ExitRoomMsg exitRoomMsg = new ExitRoomMsg();
        exitRoomMsg.setRoomId(roomid);
        sendData(exitRoomMsg);
    }

    public void requestPatchedMouseMsg(String docid, int pageid) {
        Logger.d("staticRequest requestPatchedMouseMsg start" + "docid:" + docid + " pageId:" + pageid);
        this.pageid = pageid;
        this.docid = docid;
        internalRequestPatchedMouseMsg();
    }

    private void internalRequestPatchedMouseMsg() {
        Logger.d("staticRequest internalRequestPatchedMouseMsg start docid:" + docid + " pm:" + pm);
        if (pm != null && docid != null) {
            for (Map.Entry<Integer, RoleType> entry : pm.entrySet()) {
                if (RoleType.TEACHER == entry.getValue()) {
                    RequestPatchedMouseMsg msg = new RequestPatchedMouseMsg();
                    msg.setM_dstUserID(entry.getKey());
                    msg.setM_pageTypeId(docid);
                    msg.setPageid(pageid);
                    sendData(msg);
                    Logger.d("internalRequestPatchedMouseMsg: =========>>%s", msg);
                    break;
                }
            }
        }
    }


    public void onMessageRev(Message message) {
        pageManager = ScribbleManager.getsInstance().getOldPageManager();
        switch (message.getM_cmdId()) {
            //动态涂鸦
            case ScribbleMouseMsgNotify.ID: {
                ScribbleMouseMsgNotify msgNotify = (ScribbleMouseMsgNotify) message;
                switch (msgNotify.getM_command()) {
                    case CMD_SUBBLE:
                        if (pageManager != null) {
                            //TODO:没有上课前接受的笔画
                            pageManager.dispatchBrush(msgNotify.getBrushData());
                        }
                        break;
                    case CMD_DRAW:
                    case CMD_MOVE:
                    case CMD_DELETE:
                    case CMD_ZOOM:
                    case CMD_CLEAR_PAGE:
//                    case CMD_SHOWPAGE://TODO:先进教室此时没有初始化PageManager
                        if (pageManager != null) {
                            pageManager.dispatchCmd(msgNotify.getCmdData());
                        }
                        break;
                    default:
                        break;

                }
            }
            break;
            //进房状态返回
            case EnterRoomMsgAck.ID:
                EnterRoomMsgAck ems = (EnterRoomMsgAck) message;
                boolean status = (ems.getM_enterResult() == 0);
                ScribbleManager.getsInstance().getRoomData().setPm(ems.getM_user_premi());
                for (Map.Entry<Integer, Permission> entry : ems.getM_user_premi().entrySet()) {
                    pm.put(entry.getKey(), RoleType.TEACHER);
                }

                if (status) {
                    internalRequestPatchedMouseMsg();
                }
                if (scribbleInteractiveListener != null) {
                    scribbleInteractiveListener.onEnterRoomStatus(status);
                }
                Log.d(TAG, "onMessageRev: EnterRoomMsgAck");
                break;
            //发送静态涂鸦数据
            case RequestPatchedMouseMsg.ID:
                break;
            //接受静态涂鸦数据
            case RequestPatchedMouseMsgAck.ID:
                RequestPatchedMouseMsgAck requestPatchedMouseMsgAck = (RequestPatchedMouseMsgAck) message;
                int result = requestPatchedMouseMsgAck.getM_result();
                //-1 没有此用户；-1001 数据没有准备好
                if (result != 0) {
                    Logger.e("staticRequest PatchedMouseMsgAck failure ===>" + result + "====docid:" + requestPatchedMouseMsgAck.getM_pageTypeId());
                    return;
                }
                hitCacheBrush(requestPatchedMouseMsgAck);
                break;
            //服务器请求静态涂鸦.(目前不会走了)
            case S2C_RequestPatchedMouseMsg.ID:
                sendStaticScribbleData(message);
                break;
            case ScribbleMouseMoveMsgNotify.ID:
                if (pageManager != null) {
                    //TODO:没有上课前接受的笔画
                    pageManager.onMouseMoveData(message);
                }
                break;
            case EnterRoomMsgNotify.ID:
                EnterRoomMsgNotify enterRoomMsgNotify = (EnterRoomMsgNotify) message;
                if (pm.get(enterRoomMsgNotify.getM_userId()) == null) {
                    pm.put(enterRoomMsgNotify.getM_userId(), RoleType.values()[enterRoomMsgNotify.getM_role() - 1]);
                }
                break;
            //退房通知
            case ExitRoomMsgAck.ID:
                ExitRoomMsgAck exitRoomMsgAck = (ExitRoomMsgAck) message;
                if (scribbleInteractiveListener != null) {
                    scribbleInteractiveListener.onExitRoomStatus(exitRoomMsgAck.getM_result() == 0);
                }
                break;
            case ReStaticTuyaNotify.ID:
                ReStaticTuyaNotify reStaticTuyaNotify = (ReStaticTuyaNotify) message;
                if (!TextUtils.isEmpty(docid)) {
                    pageid = reStaticTuyaNotify.getPageid();
                    docid = reStaticTuyaNotify.getDocid();
                    internalRequestPatchedMouseMsg();
                }
                Logger.d("staticRequest ReStaticNotify" + " reStatic pageid:" + pageid + " docid:" + docid);
                break;
            default:
                break;
        }

    }

    private void hitCacheBrush(RequestPatchedMouseMsgAck requestPatchedMouseMsgAck) {

        if (requestPatchedMouseMsgAckCache.isBurshIsComplete()) {
            if (pageManager != null) {
                pageManager.dispatchBrushList(requestPatchedMouseMsgAckCache.getBrushDataList());
                requestPatchedMouseMsgAckCache.setBurshIsComplete(false);
                requestPatchedMouseMsgAckCache.getBrushDataList().clear();
            }
        }

        requestPatchedMouseMsgAckCache.getBrushDataList().addAll(requestPatchedMouseMsgAck.getBrushDataList());

        int count = requestPatchedMouseMsgAckCache.getBrushDataList().size();
        if (count > 0 && count >= requestPatchedMouseMsgAck.getM_totalMsgCnt()) {
            if (pageManager != null) {
                pageManager.dispatchBrushList(requestPatchedMouseMsgAckCache.getBrushDataList());
                requestPatchedMouseMsgAckCache.setBurshIsComplete(false);
                requestPatchedMouseMsgAckCache.getBrushDataList().clear();
            } else {
                requestPatchedMouseMsgAckCache.setBurshIsComplete(true);
            }
        }
    }

    private void onDataReceived(ByteBufferList bbl) {
        if (!bbl.hasRemaining()) {
            return;
        }
        while (bbl.hasRemaining()) {
            if (m_pendingPackageData == null) {
                m_pendingPackageData = new ByteArrayOutputStream();
            }
            int dataLeftLen = bbl.remaining();
            int pendingLen = m_pendingPackageData.size();
            try {
                switch (m_readState) {
                    case STATE_READHEADER: {
                        int dataReadForHeader = PACKAGE_HEADER_LEN - pendingLen;
                        if (dataLeftLen >= dataReadForHeader) {
                            m_pendingPackageTotalLen = bbl.peekInt();
                            m_pendingPackageData.write(bbl.getBytes(dataReadForHeader));
                            m_readState = STATE_READBODY;
                            continue;
                        } else {
                            m_pendingPackageData.write(bbl.getAllByteArray());
                        }
                        break;
                    }
                    case STATE_READBODY: {
                        if (m_pendingPackageTotalLen <= 0) {
                            resetStateForPackageRead();
                            continue;
                        }

                        int dataReadForBody = m_pendingPackageTotalLen - pendingLen;
                        if (dataLeftLen >= dataReadForBody) {
                            m_pendingPackageData.write(bbl.getBytes(dataReadForBody));
                            byte bs[] = m_pendingPackageData.toByteArray();
                            packageReader(bs);
                            resetStateForPackageRead();
                            continue;
                        } else {
                            m_pendingPackageData.write(bbl.getAllByteArray());
                        }
                        break;
                    }
                    default:
                        break;
                }
            } catch (IOException e) {
                Logger.e(e.getMessage());
            }
        }
    }

    private void sendStaticScribbleData(Message msg) {
        S2C_RequestPatchedMouseMsg s2C_requestPatchedMouseMsg = (S2C_RequestPatchedMouseMsg) msg;
        S2C_RequestPatchedMouseMsgAck msgAck = new S2C_RequestPatchedMouseMsgAck();
        msgAck.setM_srcUserID(s2C_requestPatchedMouseMsg.getM_srcUserID());
        sendData(msgAck);
    }

    private void packageReader(byte[] datas) {
        ByteBuffer cbb = ByteBufferList.obtain(datas.length);
        cbb.put(datas);
        cbb.flip();
        ByteBufferList bb = new ByteBufferList();
        bb.add(cbb);

        byte[] bytes = bb.peekBytes(8);
        ByteBufferList tbb = new ByteBufferList(bytes);

        int packageLen = tbb.getInt();
        int cmdId = tbb.getInt();

        Message message = MessageGenerator.createMessageByCmdId(cmdId);
        try {
            if (message != null) {
                message.read(bb);
                onMessageRev(message);
            }
        } catch (Exception e) {
            Logger.d("[Scribble] read msg error -> %s", Log.getStackTraceString(e));
        }
        Logger.d("[Scribble] Received Message =====> CMD:%d  REMAINING:%d  MESSAGE_TYPE:%s  DATA:%s", cmdId, bb.remaining(), message, Arrays.toString(bb.getAllByteArray()));
        bb.recycle();

    }

    void resetStateForPackageRead() {
        m_readState = STATE_READHEADER;
        m_pendingPackageData.reset();
        m_pendingPackageTotalLen = 0;
    }

    private void enterRoom() {
        Logger.d("Socket connect success!");
        roomState = RoomState.STATE_ENTERROOM_CONNECTED;
        EnterRoomMsg enterRoomMsg = new EnterRoomMsg();
        enterRoomMsg.setRoomId(roomid);
        enterRoomMsg.setRole(role);
        enterRoomMsg.setRoomType(roomType);
        enterRoomMsg.setUserId(userId);
        sendData(enterRoomMsg);
    }


    public void release() {
        exitRoom();
        roomConnection.release();

    }

    public int getUserId() {
        return this.userId;
    }


    @Override
    public void onConnected(AsyncSocket socket, String host, int port) {
        this.socket = socket;
        enterRoom();
    }

    @Override
    public void onDataCallBack(ByteBufferList bb) {
        onDataReceived(bb);
    }

    @Override
    public void onEndCallBack() {

    }

    @Override
    public void onCloseCallBack() {

    }

    @Override
    public void onHeartBagSend() {
        ScribbleClearMsg msg = new ScribbleClearMsg();
        sendData(msg);
    }
}
