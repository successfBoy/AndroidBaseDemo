package com.lpc.androidbasedemo.scribble.entity;

/**
 * Created by liuliwan on 2018/5/28.
 */

public class IpAddress {
    private String ip;
    private int port;
    public IpAddress(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    public void setIp(String ip){
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}
