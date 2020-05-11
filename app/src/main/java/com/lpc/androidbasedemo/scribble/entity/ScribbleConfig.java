package com.lpc.androidbasedemo.scribble.entity;

/**
 * @author wangyh
 * @date 2019-08-12
 */
public class ScribbleConfig {
    private boolean isNewService;
    private String roomId;
    private int userId;
    private String userName;
    private String serviceUrl;
    private int servicePort;
    private int role;
    private int roomType;

    public boolean isNewService() {
        return isNewService;
    }

    public void setNewService(boolean newService) {
        isNewService = newService;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
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
}