package com.lpc.androidbasedemo.scribble.newservice;

import android.text.TextUtils;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.CompletedCallback;
import com.lpc.androidbasedemo.scribble.entity.BrushData;
import com.lpc.androidbasedemo.scribble.entity.EnterRoomMsg;
import com.lpc.androidbasedemo.scribble.entity.ExitRoomMsg;
import com.lpc.androidbasedemo.scribble.entity.IpAddress;
import com.lpc.androidbasedemo.scribble.entity.Message;
import com.lpc.androidbasedemo.scribble.entity.MouseMoveData;
import com.lpc.androidbasedemo.scribble.entity.RequestPatchedMouseMsg;
import com.lpc.androidbasedemo.scribble.entity.RequestPatchedMouseMsgAck;
import com.lpc.androidbasedemo.scribble.entity.S2C_RequestPatchedMouseMsg;
import com.lpc.androidbasedemo.scribble.entity.S2C_RequestPatchedMouseMsgAck;
import com.lpc.androidbasedemo.scribble.entity.ScribbleMouseMoveMsgNotify;
import com.lpc.androidbasedemo.scribble.entity.TCPHeader;
import com.lpc.androidbasedemo.scribble.entity.WCRSocketMessage;
import com.lpc.androidbasedemo.scribble.protocol.ClientLbsAccPacket.ClientPacketAcc;
import com.lpc.androidbasedemo.scribble.protocol.ClientPacketClass.ClientPacketClass;
import com.lpc.androidbasedemo.scribble.protocol.ClientPacketLogin.ClientPacketLogin;
import com.lpc.androidbasedemo.scribble.protocol.ClientPacketScribble.ClientPacketScribble;
import com.lpc.androidbasedemo.scribble.utils.BrushCmd;
import com.lpc.androidbasedemo.scribble.utils.BrushState;
import com.lpc.androidbasedemo.scribble.utils.DataStructureMapping;
import com.lpc.androidbasedemo.scribble.utils.DynamicByteBuffer;
import com.lpc.androidbasedemo.scribble.utils.RoleType;
import com.lpc.androidbasedemo.scribble.utils.RoomState;
import com.lpc.androidbasedemo.scribble.utils.ScribbleInteractiveListener;
import com.lpc.androidbasedemo.scribble.utils.Utils;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lpc.androidbasedemo.scribble.newservice.RoomService.ReadState.STATE_READBODY;
import static com.lpc.androidbasedemo.scribble.newservice.RoomService.ReadState.STATE_READHEADER;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_ACC_CONNECT;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_ACC_CONNECT_INFO;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_CLASS_ENTER;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_CLASS_LEAVE_FORCED_NOTI;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_DOODLE_DYNAMIC;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_DOODLE_DYNAMIC_SYNC;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_DOODLE_MESSAGE_CHANGE_SYNC;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_DOODLE_MOUSEMOVE_SYNC;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_DOODLE_STATE_UPDATE;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_DOODLE_STATE_UPDATE_SYNC;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_DOODLE_STATIC;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_LOGIN;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_LOGIN_CLIENT_HEARTBEAT;
import static com.lpc.androidbasedemo.scribble.utils.Constants.ScribbleMessage.MESSAGE_LOGOUT_FORCED;
/**
 * @author wangzhiyuan
 * @date 2016/11/14
 * desc:
 */
public class RoomService implements RoomConnection.AsyncSocketListener {

    private static final String DOC_ID = "DOC_ID";
    private static final String PAGE_ID = "PAGE_ID";
    private static final String TAG = "RoomService";
    /**
     * 前两位为head_flag
     */
    private static final int PACKAGE_HEADER_LEN = 2 + 4;
    /**
     * 包头长度
     */
    private static final int HEADER_LEN = 47;
    private int m_pendingPackageTotalLen;
    private RoomConnection roomConnection;
    private ByteArrayOutputStream m_pendingPackageData;
    private ReadState m_readState = STATE_READHEADER;
    private String roomid;
    private Map<Integer, RoleType> pm;
    private PageManager pageManager;
    private RequestPatchedMouseMsgAck requestPatchedMouseMsgAckCache = new RequestPatchedMouseMsgAck();
    private int pageid = 0;
    private String docid = "0";
    private AsyncSocket socket;
    private ScribbleInteractiveListener scribbleInteractiveListener;
    private RoomState roomState;
    private int role;
    private int roomType;
    private int userId;
    private List<ClientPacketAcc.ServerInfo> serverInfos;
    /**
     * if get server info success
     */
    private boolean isGetServerInfoSucc = false;
    /**
     * the seq number to get static scribble data
     */
    private int seq = 0;

    /**
     * 静态涂鸦请求的参数
     */
    private int mMaxRdsId = -1;
    private int mRdsIdx = 0;
    /**
     * 大画笔偏移
     */
    private int mOffset = 0;
    private int strokeType = 0;
    private int msgStatus = 0;
    private List<BrushData> brushDataList = new ArrayList<>();
    private boolean isStaticReqEnd = false;

    enum ReadState {
        STATE_READHEADER,
        STATE_READBODY
    }

    public RoomService() {
        roomConnection = new RoomConnection();
        roomConnection.setAsyncSocketistener(this);
        pm = new HashMap<>();
    }

    public void sendData(final byte[] data) {
        Logger.d("[Client] start sendData() %s", "  " + Arrays.toString(data));
        if (socket == null) {
            Logger.d("[Client] fail wrote message %s", "socket == null");
            return;
        }
        Util.writeAll(socket, data, new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    Logger.e(ex.getMessage());
                }
                Logger.d("[Client] Successfully wrote message %s", Utils.bytesToHex(data));
            }
        });
    }

    public void sendData(final Message message) {
        Logger.d("[Client] old start sendData() %s", "--" + message.toString());
        if (socket == null) {
            return;
        }
        Util.writeAll(socket, message.write(), new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) {
                    Logger.e(ex.getMessage());
                }
                Logger.d("[Client] old Successfully wrote message %s", message);
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
        this.pageid = pageid;
        this.docid = docid;

        internalRequestPatchedMouseMsg();
    }

    private void internalRequestPatchedMouseMsg() {
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
                            byte[] bytes = bbl.peekBytes(6);
                            ByteBufferList tbb = new ByteBufferList(bytes);
                            short head_flag = tbb.getShort();
                            m_pendingPackageTotalLen = tbb.getInt();
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
                            new_packageReader(bs);
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

    /**
     * public short head_flag = 0x03D9;
     * public int size;
     * public int sequence;
     * public int cmd_id;
     * public short proto_ver;
     * public byte client_type;
     * public byte conn_type;
     * public byte packet_type;
     * public byte mgs_type;
     * public byte crypt_type;
     * public long source;
     * public long target;
     * public short send_target_num;
     * public long reserved;
     *
     * @param datas
     */
    private void new_packageReader(byte[] datas) {
        ByteBuffer cbb = ByteBufferList.obtain(datas.length);
        cbb.put(datas);
        cbb.flip();
        ByteBufferList bb = new ByteBufferList();
        bb.add(cbb);
        TCPHeader tcpHeader = readHeader(bb);
        byte[] body = bb.getBytes(bb.remaining() - 1);
        final WCRSocketMessage socketMessage = createMessage(tcpHeader, body);

        dispatchMessage(socketMessage);
    }

    /**
     * 服务器消息接受
     *
     * @param message
     */
    private void dispatchMessage(WCRSocketMessage message) {
        Log.d("dispatchMessage", "cmdId:" + message.getCmdId());
        switch (message.getCmdId()) {
            case MESSAGE_ACC_CONNECT_INFO:
                responseConnectInfo(message);
                break;
            case MESSAGE_ACC_CONNECT:
                responseAccServer(message);
                break;
            case MESSAGE_LOGIN:
                responseLoginIn(message);
                break;
            case MESSAGE_CLASS_ENTER:
                responseEnterClass(message);
                break;
            case MESSAGE_DOODLE_STATIC:
                responseStaticScribbleData(message);
                break;
            case MESSAGE_DOODLE_DYNAMIC_SYNC:
                responseDynamicScribbleData(message);
                break;
            case MESSAGE_DOODLE_MESSAGE_CHANGE_SYNC:
                responseUpdateScribbleData(message);
                break;
            case MESSAGE_DOODLE_STATE_UPDATE_SYNC:
                responseStartUpdateSync(message);
                break;
            case MESSAGE_LOGOUT_FORCED:
                responseForceLogout(message);
                break;
            case MESSAGE_DOODLE_DYNAMIC:
                responseDoodleDynamicData(message);
                break;
            case MESSAGE_DOODLE_MOUSEMOVE_SYNC:
                responseMouseMoveData(message);
                break;
            //强制离开教室
            case MESSAGE_CLASS_LEAVE_FORCED_NOTI:
                break;
            default:
                break;
        }
    }

    private void responseConnectInfo(WCRSocketMessage message) {
        try {
            ClientPacketAcc.ConnetLbs_S2C s2C = ClientPacketAcc.ConnetLbs_S2C.parseFrom(message.getBody());
            serverInfos = s2C.getServerInfoList();
            Logger.d("responseConnectInfo serverInfos:--->size %s"
                    , serverInfos.size());
            if (serverInfos != null && serverInfos.size() > 0) {
                isGetServerInfoSucc = true;
                List<IpAddress> addressList = new ArrayList<>();
                int ipSize = serverInfos.size();
                for (int i = 0; i < ipSize; i++) {
                    String ip = Utils.ipConvert(serverInfos.get(i).getIp());
                    for (int j = 0; j < serverInfos.get(i).getPortList().size(); j++) {
                        int port = serverInfos.get(i).getPort(j);
                        IpAddress ipAddress = new IpAddress(ip, port);
                        addressList.add(ipAddress);
                    }
                }
                roomConnection.setIpAddressList(addressList);
                String host = Utils.ipConvert(serverInfos.get(0).getIp());
                int port = serverInfos.get(0).getPort(0);
                roomConnection.release();
                roomConnection.connect(host, port);
            }


        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void responseAccServer(WCRSocketMessage message) {
        try {
            ClientPacketLogin.ConnectAcc_S2C s2C1 = ClientPacketLogin.ConnectAcc_S2C.parseFrom(message.getBody());
            Logger.d("responseAccServer code%s -- Key:%s", s2C1.getRspCode(), s2C1.getKey());
            //接入服务成功
            if (s2C1.getRspCode() == 0) {
                requestLoginIn();
            } else {

            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void responseLoginIn(WCRSocketMessage message) {
        try {
            ClientPacketLogin.ConnectAcc_S2C s2C1 = ClientPacketLogin.ConnectAcc_S2C.parseFrom(message.getBody());
            Logger.d("responseLoginIn rspCode:%s", s2C1.getRspCode());
            //登录成功
            if (s2C1.getRspCode() == 0) {
                requestEnterClass();
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void responseForceLogout(WCRSocketMessage message) {
        try {
            ClientPacketLogin.ForceLogout_S2C s2C = ClientPacketLogin.ForceLogout_S2C.parseFrom(message.getBody());
            Logger.e("responseForceLogout rsp_code:%s reason:%s", s2C.getRspCode(), s2C.getReason());
            //0其它端踢掉 1管理员踢掉 2心跳超时
            int reason = s2C.getReason();
            Logger.e("responseForceLogout %s", reason);
            roomConnection.release();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void responseForceLeaveClass(WCRSocketMessage message) {
        try {
            ClientPacketClass.ForceLeaveClass_S2C s2C = ClientPacketClass.ForceLeaveClass_S2C.parseFrom(message.getBody());
            Logger.e("responseForceLeaveClass reason:%s", s2C.getReason());
            roomConnection.release();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void responseEnterClass(WCRSocketMessage message) {
        try {
            ClientPacketClass.EnterClass_S2C s2c = ClientPacketClass.EnterClass_S2C.parseFrom(message.getBody());
            Logger.d("responseEnterClass rspCode:%s", s2c.getRspCode());
            boolean status = s2c.getRspCode() == 0;
            if (scribbleInteractiveListener != null) {
                scribbleInteractiveListener.onEnterRoomStatus(status);
            }
            //进房成功
            if (status) {
                requestStaticScribbleData(docid, pageid);
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受静态涂鸦数据
     *
     * @param message
     */
    private void responseStaticScribbleData(WCRSocketMessage message) {
        try {
            ClientPacketScribble.ReqScribbleMsg_S2C s2c = ClientPacketScribble.ReqScribbleMsg_S2C.parseFrom(message.getBody());
            Logger.d("staticScribbleData rspCode:%s", s2c.getRspCode() + " docid:" + docid + " pageId:" + pageid + " liveui_response");
            if (s2c.getRspCode() != 0) {
                //异常处理
                Logger.e("staticScribbleData error rspCode:%s", s2c.getRspCode());
                return;
            }
            String pageTypeIdLocal = this.docid + ":" + this.pageid;
            if (!pageTypeIdLocal.equals(s2c.getPageTypeId())) {
                Logger.e("staticScribbleData error pageTypeIdLocal:" + pageTypeIdLocal + " pageTypeIdRemote:" + s2c.getPageTypeId());
                return;
            }
            //获取静态涂鸦成功
            Logger.d("staticScribbleData", "responseScribbleData BrushDataSize:" + s2c.getBrushDataList().size() + " seq:" + s2c.getSeq() + " msg_status:" + s2c.getMsgStatus() +
                    "offset:" + s2c.getOffset() + " max_data:" + s2c.getMaxData() + " stroke_type:" + s2c.getStrokeType() + " max_rds_id:" + s2c.getMaxRdsId() + " cid:" + s2c.getCid());
            if (s2c.getSeq() != seq - 1) {
                //requestStaticScribbleDataInner(docid,pageid);
                Logger.e("responseStatic seq:" + seq + " s2c.getSeq:" + s2c.getSeq());
                return;
            }
            pageManager = ScribbleManager.getsInstance().getNewPageManager();
            mMaxRdsId = s2c.getMaxRdsId();
            mOffset = s2c.getOffset();
            msgStatus = s2c.getMsgStatus();
            strokeType = s2c.getStrokeType();
            for (int i = 0; i < s2c.getBrushDataCount(); i++) {
                mRdsIdx = s2c.getBrushData(i).getRdsIdx();
                BrushData brushData = DataStructureMapping.brushDataMapping(s2c.getBrushData(i), MESSAGE_DOODLE_STATIC);
                brushData.setM_status(BrushState.BRUSH_START);
                brushDataList.add(brushData);
            }
            //继续请求大笔画
            if (mOffset < s2c.getMaxData() && s2c.getMsgStatus() != 0) {
                mOffset++;
                requestStaticScribbleDataInner(docid, pageid);
                return;
            }
            if (mRdsIdx < mMaxRdsId) {
                mRdsIdx++;
                requestStaticScribbleDataInner(docid, pageid);
            } else { //请求结束，开始显示
                pageManager.dispatchBrushList(brushDataList);
                isStaticReqEnd = true;
                Logger.d("staticScribbleData response success");

            }


        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void responseDynamicScribbleData(WCRSocketMessage message) {
        try {
            ClientPacketScribble.BrushDataEx s2c = ClientPacketScribble.BrushDataEx.parseFrom(message.getBody());
            pageManager = ScribbleManager.getsInstance().getNewPageManager();
            String pageTypeId = s2c.getPageTypeId();
            Logger.d(" responseDynamicScribbleData brushCmd: %s showFlag:%s", BrushCmd.values()[s2c.getCommand()], s2c.getBrushData().getShowFlag());
            switch (BrushCmd.values()[s2c.getCommand()]) {
                case CMD_SUBBLE:
                    if (pageManager != null) {
                        String pageTypeIdLocal = docid + ":" + pageid;
                        if (pageTypeIdLocal.equals(pageTypeId)) {
                            pageManager.dispatchBrush(DataStructureMapping.brushDataMapping(s2c, MESSAGE_DOODLE_DYNAMIC_SYNC));
                        }
                    }
                    break;
                default:
                    break;
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void responseUpdateScribbleData(WCRSocketMessage message) {
        try {
            ClientPacketScribble.NotifyUpdateScribbleMsg_S2C s2c = ClientPacketScribble.NotifyUpdateScribbleMsg_S2C.parseFrom(message.getBody());
            pageManager = ScribbleManager.getsInstance().getNewPageManager();
            int size = s2c.getBrushDataList().size();
            Logger.d("responseUpdateScribbleData brushDataPB cmd:%s brushDataPb:%s", size, s2c.getBrushDataList().get(0).getCommand(), s2c.getBrushDataList().get(0).getBrushData().toString());
            for (int i = 0; i < size; i++) {
                if (BrushCmd.values()[s2c.getBrushDataList().get(i).getCommand()] == BrushCmd.CMD_MOVE ||
                        BrushCmd.values()[s2c.getBrushDataList().get(i).getCommand()] == BrushCmd.CMD_ZOOM) {
                    pageManager.dispatchCmd(DataStructureMapping.cmdDataMapping(s2c, MESSAGE_DOODLE_MESSAGE_CHANGE_SYNC));

                } else {
                    pageManager.dispatchBrush(DataStructureMapping.brushDataMapping(s2c.getBrushDataList().get(i), MESSAGE_DOODLE_MESSAGE_CHANGE_SYNC));
                }
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void responseStartUpdateSync(WCRSocketMessage message) {
        try {
            ClientPacketScribble.NotifyScribbleStatus_S2C s2c = ClientPacketScribble.NotifyScribbleStatus_S2C.parseFrom(message.getBody());
            pageManager = ScribbleManager.getsInstance().getNewPageManager();
            int size = s2c.getScribbleIdCount();
            Logger.d("responseStartUpdateSync brushDataPB size:%s rdsId:%s userDrawId:%s uid:%s", size, s2c.getScribbleId(0).getRdsIdx(), s2c.getScribbleId(0).getUidDrawId(), s2c.getUid());
            pageManager.dispatchCmd(DataStructureMapping.cmdDataMapping(s2c));

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新自己画过的涂鸦ID
     *
     * @param message
     */
    private void responseDoodleDynamicData(WCRSocketMessage message) {
        try {
            ClientPacketScribble.ScribbleMouseMsg_S2C s2c = ClientPacketScribble.ScribbleMouseMsg_S2C.parseFrom(message.getBody());
            int rdsId = s2c.getRdsIdx();
            Log.d("response", "requestDynamicScribbleData responseDoodleDynamicData rdsID:" + rdsId + " draw_id:" + s2c.getUidDrawId() + " rspCode:" + s2c.getRspCode());
            pageManager = ScribbleManager.getsInstance().getNewPageManager();
            pageManager.getScribbleView().setCurrentBrushDataUseDrawID(s2c.getUidDrawId());
            pageManager.getScribbleView().updateM_id(rdsId);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受激光笔移动消息
     *
     * @param message
     */
    public void responseMouseMoveData(WCRSocketMessage message) {
        try {
            ClientPacketScribble.NotifyScribbleMouseMove_S2C s2C = ClientPacketScribble.NotifyScribbleMouseMove_S2C.parseFrom(message.getBody());
            MouseMoveData moveData = DataStructureMapping.mouseMoveDataMapping(s2C.getMouseData());
            ScribbleMouseMoveMsgNotify notify = new ScribbleMouseMoveMsgNotify();
            notify.setData(moveData);
            pageManager = ScribbleManager.getsInstance().getNewPageManager();
            pageManager.onMouseMoveData(notify);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }


    private void requestConnectInfo() {
        ClientPacketAcc.ConnetLbs_C2S c2S = ClientPacketAcc.ConnetLbs_C2S.newBuilder().build();
        WCRSocketMessage socketMessage = new WCRSocketMessage(MESSAGE_ACC_CONNECT_INFO, c2S.toByteArray());
        socketSendMessage(socketMessage);

    }

    private void requestAccessServer() {
        ClientPacketLogin.ConnectAcc_C2S c2s = ClientPacketLogin.ConnectAcc_C2S.newBuilder()
                .setClientType(3)
                .setClientNetType(2)
                .setClientOsFlag(26)
                .build();
        WCRSocketMessage socketMessage = new WCRSocketMessage(MESSAGE_ACC_CONNECT, c2s.toByteArray());
        socketSendMessage(socketMessage);
        Logger.d("requestAccessServer %s", "requestAccessServer end");
    }

    private void requestLoginIn() {
        ClientPacketLogin.Login_C2S c2s1 = ClientPacketLogin.Login_C2S.newBuilder()
                .setAccount(String.valueOf(userId))
                .setAccountType(0)
                .setDefaultStatus(0)
                .build();
        WCRSocketMessage socketMessage = new WCRSocketMessage(MESSAGE_LOGIN, c2s1.toByteArray());
        socketSendMessage(socketMessage);
        Logger.d("requestLoginIn %s", "requestLoginIn end");
    }

    private void requestEnterClass() {
        ClientPacketClass.EnterClass_C2S class_c2S = ClientPacketClass.EnterClass_C2S.newBuilder()
                .setClassPlayMode(0)
                .setClassType(roomType)
                .setCid(Integer.valueOf(roomid).longValue())
                .setStrCid(roomid)
                .setUsrRole(role)
                .build();
        WCRSocketMessage socketMessage = new WCRSocketMessage(MESSAGE_CLASS_ENTER, class_c2S.toByteArray());
        socketSendMessage(socketMessage);
        Logger.d("requestEnterClass %s", "requestEnterClass end");
    }

    private void requestStaticScribbleDataInner(String docId, int pageid) {
        this.docid = docId;
        this.pageid = pageid;
        isStaticReqEnd = false;
        String pageTypeId = docId + ":" + pageid;
        Logger.d("staticScribbleData %s", "requestStaticScribbleDataInner docId:"
                + docid + " pageId:" + pageid + " userid:" + userId + " roomid:" + roomid + " seq:" + seq + " mRdsIdx:" + mRdsIdx
                + " mOffset:" + mOffset + " strokeType:" + strokeType + " msgStatus:" + msgStatus);
        ClientPacketScribble.ReqScribbleMsg_C2S c2s = ClientPacketScribble.ReqScribbleMsg_C2S.newBuilder()
                .setUid(Integer.valueOf(userId).longValue())
                .setCid(Integer.valueOf(roomid).longValue())
                .setSeq(seq++)
                .setRdsId(mRdsIdx)
                .setOffset(mOffset)
                .setStrokeType(strokeType)
                .setPageTypeId(pageTypeId)
                .setMsgStatus(msgStatus)
                .build();
        WCRSocketMessage socketMessage = new WCRSocketMessage(MESSAGE_DOODLE_STATIC, c2s.toByteArray());
        socketSendMessage(socketMessage);
    }

    public void requestStaticScribbleData(String docid, int pageid) {
        Logger.d("staticScribbleData %s", "start requestStaticScribbleData docid:"
                + docid + " pageId:" + pageid + " isConnect:" + roomConnection.socketIsConnect());
        if (!roomConnection.socketIsConnect()) {
            this.docid = docid;
            this.pageid = pageid;
            return;
        }
        if (brushDataList != null) {
            brushDataList.clear();
        }
        //the seq number to get static scribble data
        seq = 0;
        //静态涂鸦请求的参数重置
        mMaxRdsId = -1;
        mRdsIdx = 0;
        //大画笔偏移
        mOffset = 0;
        strokeType = 0;
        msgStatus = 0;
        requestStaticScribbleDataInner(docid, pageid);
    }

    public void requestDynamicScribbleData(BrushData brushData) {
        if (!TextUtils.isEmpty(roomid)) {
            try {
                ClientPacketScribble.BrushData brushDataPb = DataStructureMapping.convertPbBrushData(brushData);
                Logger.d("requestDynamicScribbleData %s", "requestDynamicScribbleData brushPageTypeId: "
                        + brushDataPb.getPageTypeId() + " pageId:" + brushDataPb.getPageId() + " id:" + brushDataPb.getId() + " roomid:" + roomid);
                ClientPacketScribble.ScribbleMouseMsg_C2S c2S = ClientPacketScribble.ScribbleMouseMsg_C2S.newBuilder()
                        .setCid(Integer.valueOf(roomid).longValue())
                        .setBrushData(brushDataPb)
                        .setCommand(0)
                        .setRdsIdx(brushDataPb.getId())
                        .setPageTypeId(brushDataPb.getPageTypeId())
                        .setUid(Integer.valueOf(userId).longValue())
                        .build();
                WCRSocketMessage socketMessage = new WCRSocketMessage(MESSAGE_DOODLE_DYNAMIC, c2S.toByteArray());
                socketSendMessage(socketMessage);
            } catch (Exception e) {
                Logger.e("requestDynamicScribbleData %s", " ---> Exception: " + e.getMessage());
            }
        }
    }

    public void requestUpdateScribbleData(BrushData brushData, boolean isBackOrForward) {//isBackOrForward == true 撤销
        int command = 4;
        int msgStatus = 2;
        if (!isBackOrForward) {
            command = 1;
            msgStatus = 1;
        }
        ClientPacketScribble.BrushData brushDataPb = DataStructureMapping.convertPbBrushData(brushData);

        ClientPacketScribble.ScribbleId scribbleId = ClientPacketScribble.ScribbleId.newBuilder()
                .setMsgStatus(msgStatus)
                .setUidDrawId(brushData.getUserDrawId())
                .setRdsIdx(brushData.getM_id())
                .build();
        ClientPacketScribble.UpdateScribbleStatus_C2S c2S = ClientPacketScribble.UpdateScribbleStatus_C2S.newBuilder()
                .setUid(Integer.valueOf(userId).longValue())
                .setCid(Integer.valueOf(roomid).longValue())
                .setCommand(command)
                .setPageTypeId(brushDataPb.getPageTypeId())
                .addScribbleId(scribbleId)
                .build();
        WCRSocketMessage socketMessage = new WCRSocketMessage(MESSAGE_DOODLE_STATE_UPDATE, c2S.toByteArray());
        socketSendMessage(socketMessage);
    }

    private WCRSocketMessage createMessage(TCPHeader tcpHeader, byte[] body) {
        WCRSocketMessage socketMessage = new WCRSocketMessage(tcpHeader.cmd_id, body);
        return socketMessage;
    }

    private TCPHeader readHeader(ByteBufferList bb) {
        TCPHeader tcpHeader = new TCPHeader();
        tcpHeader.head_flag = bb.getShort();
        tcpHeader.size = bb.getInt();
        tcpHeader.sequence = bb.getInt();
        tcpHeader.cmd_id = bb.getInt();
        tcpHeader.proto_ver = bb.getShort();
        tcpHeader.client_type = bb.get();
        tcpHeader.conn_type = bb.get();
        tcpHeader.packet_type = bb.get();
        tcpHeader.mgs_type = bb.get();
        tcpHeader.crypt_type = bb.get();
        tcpHeader.source = bb.getLong();
        tcpHeader.target = bb.getLong();
        tcpHeader.send_target_num = bb.getShort();
        tcpHeader.reserved = bb.getLong();
        return tcpHeader;
    }

    /**
     * 数据统一发送入口
     *
     * @param message
     */
    private void socketSendMessage(WCRSocketMessage message) {
        DynamicByteBuffer buffer = encodeData(message);
        sendData(buffer.array());
        Logger.d("socketSendMessage %s", "socketSendMessage--->" + " cmdId:" + message.getCmdId() + "size:" + buffer.array().length + " " + Utils.bytesToHex(buffer.array()));
    }

    private DynamicByteBuffer encodeData(WCRSocketMessage message) {
        if (message.getBody() == null || message.getCmdId() == 0) {
            Logger.e("encodeData %s", "message.getBody()==" + message.getBody());
            return null;
        }
        DynamicByteBuffer buffer = new DynamicByteBuffer();
        //包头
        createHeaderData(buffer, message);
        //包体
        buffer.put(message.getBody());
        //包尾
        byte TailFlag = (byte) 0xDE;
        buffer.put(TailFlag);
        //自适应数组长度
        DynamicByteBuffer expanded = new DynamicByteBuffer(buffer.position());
        expanded.put(buffer.array(), 0, expanded.capacity());

        return expanded;
    }

    private void createHeaderData(DynamicByteBuffer buffer, WCRSocketMessage message) {
        TCPHeader tcpHeader = new TCPHeader();
        int headerLength = 47;
        tcpHeader.size = message.getBody().length + headerLength + 1;
        tcpHeader.sequence = 0;
        tcpHeader.cmd_id = message.getCmdId();
        tcpHeader.proto_ver = 0;
        tcpHeader.client_type = 3;
        tcpHeader.conn_type = 0;
        tcpHeader.packet_type = 2;
        tcpHeader.mgs_type = 0;
        tcpHeader.crypt_type = 0;
        tcpHeader.source = 0;
        tcpHeader.target = 0;
        tcpHeader.send_target_num = 0;
        tcpHeader.reserved = 0;
        buffer.putShort(tcpHeader.head_flag);
        buffer.putInt(tcpHeader.size);
        buffer.putInt(tcpHeader.sequence);
        buffer.putInt(tcpHeader.cmd_id);
        buffer.putShort(tcpHeader.proto_ver);
        buffer.put(tcpHeader.client_type);
        buffer.put(tcpHeader.conn_type);
        buffer.put(tcpHeader.packet_type);
        buffer.put(tcpHeader.mgs_type);
        buffer.put(tcpHeader.crypt_type);
        buffer.putLong(tcpHeader.source);
        buffer.putLong(tcpHeader.target);
        buffer.putShort(tcpHeader.send_target_num);
        buffer.putLong(tcpHeader.reserved);
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
        Logger.d("onConnected %s", "onConnected" + "isGetServerInfoSucc:" + isGetServerInfoSucc);
        if (!isGetServerInfoSucc) {
            requestConnectInfo();
        } else {
            requestAccessServer();
        }
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
        ClientPacketLogin.ClientHeart_C2S c2s1 = ClientPacketLogin.ClientHeart_C2S.newBuilder()
                .build();
        WCRSocketMessage socketMessage = new WCRSocketMessage(MESSAGE_LOGIN_CLIENT_HEARTBEAT, c2s1.toByteArray());
        socketSendMessage(socketMessage);
    }

    public boolean isStaticReqEnd() {
        return isStaticReqEnd;
    }
}
