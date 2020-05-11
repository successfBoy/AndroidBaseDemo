package com.lpc.androidbasedemo.scribble.utils;

import java.util.Map;

/**
 * Created by wangzhiyuan on 2016/12/8.
 * desc:
 */

public class RoomData {
    /**
     * 房间中其它用户及权限
     */
    private Map<Integer, Permission> pm;

    private int pageId=0;
    private int userId;

    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    //TODO:临时添加默认空字符串避免静态涂鸦空指针
    private String pageTypeId = "0";

    public Map<Integer, Permission> getPm() {
        return pm;
    }

    public void setPm(Map<Integer, Permission> pm) {
        this.pm = pm;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getPageTypeId() {
        return pageTypeId;
    }

    public void setPageTypeId(String pageTypeId) {
        this.pageTypeId = pageTypeId;
    }

    private boolean enableDraw;

    public boolean isEnableDraw() {
        return enableDraw;
    }

    public void setEnableDraw(boolean enableDraw) {
        this.enableDraw = enableDraw;
    }
}
