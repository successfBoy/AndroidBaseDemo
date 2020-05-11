package com.lpc.androidbasedemo.scribble.utils;

import android.graphics.Color;
import android.text.TextUtils;

import com.lpc.androidbasedemo.scribble.Pen;
import com.lpc.androidbasedemo.scribble.entity.BrushData;
import com.lpc.androidbasedemo.scribble.entity.CmdData;
import com.lpc.androidbasedemo.scribble.entity.LaserMouseMsg;
import com.lpc.androidbasedemo.scribble.entity.MouseMoveData;
import com.lpc.androidbasedemo.scribble.entity.Point;
import com.lpc.androidbasedemo.scribble.entity.ScribbleMouseMoveMsgNotify;
import com.lpc.androidbasedemo.scribble.entity.ScribbleMouseMsg;
import com.lpc.androidbasedemo.scribble.newservice.RoomService;
import com.lpc.androidbasedemo.scribble.newservice.ScribbleManager;
import com.lpc.androidbasedemo.scribble.protocol.ClientPacketScribble.ClientPacketScribble;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by liuliwan on 2018/4/26.
 * 新涂鸦协议与老涂鸦之间的数据转换
 */

public class DataStructureMapping {
    private static int i =0;

    public static BrushData brushDataMapping(ClientPacketScribble.BrushDataEx brushDataEx, int cmdId) {
        ClientPacketScribble.BrushData brushDataPB = brushDataEx.getBrushData();
        List<Point> points = new ArrayList<>();
        BrushData brushData = new BrushData();
        int pointCount = brushDataPB.getPointList().size();
        for (int i = 0; i < pointCount; i++) {
            Point pointEntity = new Point();
            pointEntity.setX((short) brushDataPB.getPointList().get(i).getX());
            pointEntity.setY((short) brushDataPB.getPointList().get(i).getY());
            pointEntity.setInterval((short) brushDataPB.getPointList().get(i).getInterval());
            points.add(pointEntity);
        }
        brushData.setM_points(points);
        brushData.setM_areaWidth((short) brushDataPB.getAreaWidth());
        brushData.setM_areaHeight((short) brushDataPB.getAreaHeight());
        brushData.setM_height((short) brushDataPB.getHeight());
        brushData.setM_width((short) brushDataPB.getWidth());
        brushData.setM_cmdId(cmdId);
        brushData.setM_id(brushDataEx.getRdsIdx());
        brushData.setUser_draw_id(brushDataPB.getId());
        try {
            List<String> pageIdStrings = Arrays.asList(brushDataPB.getPageTypeId().split(":"));
            brushData.setM_pageId(Integer.parseInt(pageIdStrings.get(1)));
            brushData.setM_pageTypeId(pageIdStrings.get(0));

        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        brushData.setM_userId((int) brushDataPB.getUid());
        brushData.setM_pCount(brushDataPB.getPointCount());
        brushData.setM_status(BrushState.getByValue(brushDataPB.getBrushStatus()));
        brushData.setM_index(brushDataPB.getIndex());
        Pen pen = new Pen();
        pen.setType(StrokeType.values()[brushDataPB.getPen().getType()]);
        pen.setColor(brushDataPB.getPen().getColor());
        pen.setRude(brushDataPB.getPen().getRude());
        brushData.setM_pen(pen);
        brushData.setM_showFlag((byte) brushDataPB.getShowFlag());
        brushData.setM_remote(brushDataPB.getRemote());
        brushData.setM_textureUrl(brushDataPB.getTextureUrl());
        brushData.setM_uploadtexture(brushDataPB.getUploadTexture());
        if (brushDataPB.getTextDrawable() != null &&
                brushDataPB.getTextDrawable().getText() != null &&
                !TextUtils.isEmpty(brushDataPB.getTextDrawable().getText().toStringUtf8())) {
            brushData.setText(brushDataPB.getTextDrawable().getText().toStringUtf8());
            if (brushDataPB.getTextDrawable().getFontName() != null)
                brushData.setFontName(brushDataPB.getTextDrawable().getFontName().toStringUtf8());
            int fontSize = brushDataPB.getTextDrawable().getFontSize();
            byte size = (byte) fontSize;
            brushData.setFontSize(size);
            byte textFlag = 1;
            brushData.setTextFlag(textFlag);
        } else {
            byte textFlag = 0;
            brushData.setTextFlag(textFlag);
        }
        return brushData;

    }
    public static  ClientPacketScribble.BrushData convertPbBrushData(BrushData brushData){
        Long uid = Integer.valueOf(brushData.getM_userId()).longValue();
        String pageTypeId = brushData.getM_pageTypeId()+":"+brushData.getM_pageId();
        int pageId = brushData.getM_pageId();
        int brush_id = brushData.getM_id();
        int brushStatus = 0;
        switch (brushData.getM_status()){
            case BRUSH_ALL:
                brushStatus =7;
                break;
            case BRUSH_OVER:
                brushStatus = 4;
                break;
            case BRUSH_START:
                brushStatus = 1;
                break;
            case BRUSH_PROCESS:
                brushStatus = 2;
                break;
            case BRUSH_INVILADE:
                brushStatus = 0;
                break;
            default:
                brushStatus = 0;
                break;
        }
        int area_width = brushData.getM_areaWidth();
        int area_height = brushData.getM_areaHeight();
        int width = brushData.getM_width();
        int height = brushData.getM_height();
        Pen pen = brushData.getM_pen();
        int penType = pen.getType().ordinal();
        ClientPacketScribble.Pen penPb = ClientPacketScribble.Pen.newBuilder()
                .setColor(pen.getColor())
                .setRude(pen.getRude())
                .setType(penType)
                .build();
        int show_flag = (int)brushData.getM_showFlag();
        String textureUrl = brushData.getM_textureUrl();
        if (textureUrl == null)
            textureUrl ="";
        boolean remote = brushData.isM_remote();
        boolean uploadTexture = brushData.isM_uploadtexture();
        int point_cnt = brushData.getM_points().size();
        List<Point> pointList = brushData.getM_points();
        List<ClientPacketScribble.Point> pointListPb = new ArrayList<>();
        for (Point p:pointList){
            ClientPacketScribble.Point pointPb = ClientPacketScribble.Point.newBuilder()
                    .setInterval(p.getInterval())
                    .setX(p.getX())
                    .setY(p.getY())
                    .build();
            pointListPb.add(pointPb);
        }
        ClientPacketScribble.BrushData brushDataPb = ClientPacketScribble.BrushData.newBuilder()
                .setUid(uid)
                .setPageTypeId(pageTypeId)
                .setPageId(pageId)
                .setId(brush_id)
                .setBrushStatus(brushStatus)
                .setAreaHeight(area_height)
                .setAreaWidth(area_width)
                .setHeight(height)
                .setWidth(width)
                .setPen(penPb)
                .setIndex(brushData.getM_index())
                .setOffset(0)
                .setShowFlag(show_flag)
                .setTextureUrl(textureUrl)
                .setRemote(remote)
                .setUploadTexture(uploadTexture)
                .setPointCnt(point_cnt)
                .addAllPoint(pointListPb)
                .build();
        return brushDataPb;
    }



    public static CmdData cmdDataMapping(ClientPacketScribble.NotifyScribbleStatus_S2C pbData){
        CmdData cmdData = new CmdData();
        cmdData.setM_cmd(BrushCmd.values()[pbData.getCommand()]);
        cmdData.setM_pageTypeId(pbData.getPageTypeId());
        String pageTypeId = pbData.getPageTypeId();
        try {
            List<String> pageIdStrings = Arrays.asList(pageTypeId.split(":"));
            cmdData.setM_pageId(Integer.parseInt(pageIdStrings.get(1)));
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        cmdData.setM_userId((int)pbData.getUid());
        int size = pbData.getScribbleIdCount();
        List<Integer> strokeIds = new ArrayList<>();
        for (int i=0; i<size; i++){
            cmdData.setM_showflag((byte)pbData.getScribbleIdList().get(i).getMsgStatus());
            strokeIds.add(new Integer(pbData.getScribbleIdList().get(i).getRdsIdx()));
        }
        cmdData.setM_strokeIds(strokeIds);
        return cmdData;
    }
    public static CmdData cmdDataMapping(ClientPacketScribble.NotifyUpdateScribbleMsg_S2C pbData,int cmdId){
        CmdData cmdData = new CmdData();
        cmdData.setM_cmd(BrushCmd.values()[pbData.getCommand()]);
        cmdData.setM_pageTypeId(pbData.getPageTypeId());
        String pageTypeId = pbData.getPageTypeId();
        try {
            List<String> pageIdStrings = Arrays.asList(pageTypeId.split(":"));
            cmdData.setM_pageId(Integer.parseInt(pageIdStrings.get(1)));

        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        cmdData.setM_userId((int)pbData.getUid());
        int size = pbData.getBrushDataList().size();
        List<Integer> strokeIds = new ArrayList<>();
        List<BrushData> brushDataList = new ArrayList<>();
        for (int i=0; i<size; i++){
            cmdData.setM_showflag((byte)pbData.getBrushData(i).getBrushData().getShowFlag());
            strokeIds.add(new Integer(pbData.getBrushData(i).getRdsIdx()));
            brushDataList.add(brushDataMapping(pbData.getBrushData(i),cmdId));
        }
        cmdData.setM_strokeIds(strokeIds);
        cmdData.setM_brushDatas(brushDataList);
        return cmdData;
    }
    public static BrushData generateBrushData(BrushState brushState, int usrDrawId, int areaWidth, int areaHeight, List<Point> pointList, StrokeType type, Pen mPen){
        BrushData data = new BrushData();
        if (ScribbleManager.getsInstance().isNew()){ //新涂鸦
            data.setM_pageId(ScribbleManager.getsInstance().getRoomData().getPageId());
            data.setM_pageTypeId(ScribbleManager.getsInstance().getRoomData().getPageTypeId());
            data.setM_userId(ScribbleManager.getsInstance().getNewRoomService().getUserId());
        }else {
            data.setM_pageId(ScribbleManager.getsInstance().getRoomData().getPageId());
            data.setM_pageTypeId(ScribbleManager.getsInstance().getRoomData().getPageTypeId());
            data.setM_userId(ScribbleManager.getsInstance().getOldRoomService().getUserId());
        }
        data.setM_id(usrDrawId);
        data.setM_status(brushState);
        if (mPen!=null){
            Pen pen = new Pen();
            pen.setType(type);
            pen.setColor(mPen.getColor());
            pen.setRude(mPen.getRude());
            data.setM_pen(pen);
        }else {
            Pen pen = new Pen();
            pen.setType(type);
            pen.setColor(Color.BLACK);
            pen.setRude(3);
            data.setM_pen(pen);
        }
        data.setM_areaWidth((short)areaWidth);
        data.setM_areaHeight((short)areaHeight);
        data.setM_width((short) 0);
        data.setM_height((short) 0);
        data.setM_pCount(pointList.size());
        if (pointList!=null){
            for (int i=0;i<pointList.size();i++){
                Point point = new Point();
                point.setInterval(pointList.get(i).getInterval());
                point.setX(pointList.get(i).getX());
                point.setY(pointList.get(i).getY());
                data.getM_points().add(point);
            }
        }
//      data.setM_index(0);
//      data.setM_offset(0);
        data.setM_showFlag((byte) 1);
        data.setM_textureUrl(null);
        data.setM_remote(false);
        data.setM_uploadtexture(false);
        //协议未显示
        data.setTextFlag((byte) 0);
        data.setText(null);
        data.setFontName(null);
        data.setFontSize((byte) 0);
        return data;
    }

    public static ScribbleMouseMoveMsgNotify generateMouseMoveData(int userId, String userName, String docId, StrokeType strokeType,
                                                                   Point point, int areaWidth, int areaHeight, boolean show) {
        ScribbleMouseMoveMsgNotify mouseMoveMsgNotify = new ScribbleMouseMoveMsgNotify();
        mouseMoveMsgNotify.setUserid(userId);
        mouseMoveMsgNotify.setPageTypeId(docId);
        MouseMoveData mouseMoveData = new MouseMoveData();
        mouseMoveData.setUserid(userId + "");
        mouseMoveData.setUsename(userName);
        mouseMoveData.setX(point.getX());
        mouseMoveData.setY(point.getY());
        mouseMoveData.setWidth(areaWidth);
        mouseMoveData.setHeight(areaHeight);
        mouseMoveData.setIsteacher(false);
        mouseMoveData.setShow(show);
        mouseMoveData.setStroketype(strokeType);
        mouseMoveMsgNotify.setData(mouseMoveData);
        return mouseMoveMsgNotify;
    }

    /**
     * 涂鸦按下时发送消息通知学生名字
     *
     * @param mouseMoveMsgNotify
     */
    public static void sendMouseMoveData(ScribbleMouseMoveMsgNotify mouseMoveMsgNotify) {
        if (mouseMoveMsgNotify == null) {
            return;
        }
        LaserMouseMsg msg = new LaserMouseMsg();
        msg.setM_targetUserID(-1);
        msg.setM_pageTypeId(mouseMoveMsgNotify.getPageTypeId());
        msg.setReq_Id(i++);
        msg.setMouseMoveData(mouseMoveMsgNotify.getData());
        if (!ScribbleManager.getsInstance().isNew()) {
            ScribbleManager.getsInstance().getOldRoomService().sendData(msg);
        }
    }

    public static MouseMoveData mouseMoveDataMapping(ClientPacketScribble.MouseMoveData mouseMoveDataPb){
        MouseMoveData moveData = new MouseMoveData();
        moveData.setX(mouseMoveDataPb.getX());
        moveData.setY(mouseMoveDataPb.getY());
        moveData.setWidth(mouseMoveDataPb.getWidth());
        moveData.setHeight(mouseMoveDataPb.getHeight());
        moveData.setUserid(String.valueOf(mouseMoveDataPb.getUid()));
        moveData.setIsteacher(mouseMoveDataPb.getIsTeacher());
        moveData.setUsename(mouseMoveDataPb.getUsrName());
        int strokeTypePb = mouseMoveDataPb.getStrokeType();
        if (strokeTypePb<StrokeType.values().length //:todo 老师退出教室重进，strokeTypePb的值很大，暂时这样处理
                &&strokeTypePb>=0){
            moveData.setStroketype(StrokeType.values()[strokeTypePb]);
        }else {
            Logger.e("mouseMoveDataMapping %s", "strokeTypePb:" + strokeTypePb);
        }
        moveData.setShow(mouseMoveDataPb.getIsShow());
        return moveData;
    }


    public static void sendBrushData(BrushData brushData){
        ScribbleMouseMsg msg = new ScribbleMouseMsg();
        msg.setM_targetUserID(-1);
        msg.setM_command(BrushCmd.CMD_SUBBLE);
        msg.setM_pageTypeId(brushData.getM_pageTypeId());
        msg.setReq_Id(i++);
        msg.setBrushData(brushData);
        if (ScribbleManager.getsInstance().isNew()){
            ScribbleManager.getsInstance().getNewRoomService().requestDynamicScribbleData(brushData);
        }else {
            ScribbleManager.getsInstance().getOldRoomService().sendData(msg);
        }
    }
    public static void sendRedoCmd(BrushData brushData,boolean isBackOrForward){
        boolean isNew = ScribbleManager.getsInstance().isNew();
        if (!isNew){
            ScribbleMouseMsg msg = new ScribbleMouseMsg();
            msg.setM_targetUserID(-1);
            msg.setM_command(BrushCmd.CMD_DRAW);
            msg.setM_pageTypeId(brushData.getM_pageTypeId());
            msg.setReq_Id(i++);
            CmdData cmdData = new CmdData();
            cmdData.setM_cmd(BrushCmd.CMD_DRAW);
            List<Integer> strokeIds = new ArrayList<>();
            strokeIds.add(new Integer(brushData.getM_id()));
            cmdData.setM_strokeIds(strokeIds);
            cmdData.setM_showflag(brushData.getM_showFlag());
            cmdData.setM_pageId(brushData.getM_pageId());
            cmdData.setM_pageTypeId(brushData.getM_pageTypeId());
            cmdData.setM_userId(brushData.getM_userId());
            msg.setBrushData(brushData);
            msg.setCmdData(cmdData);
            ScribbleManager.getsInstance().getOldRoomService().sendData(msg);
        }else {
            RoomService roomService = ScribbleManager.getsInstance().getNewRoomService();
            if (roomService!=null){
                roomService.requestUpdateScribbleData(brushData,isBackOrForward);
            }
        }
    }

}
