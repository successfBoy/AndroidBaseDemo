package com.lpc.androidbasedemo.scribble.entity;

/**
 * Created by wangzhiyuan on 2018/1/19.
 * desc:
 */

public class TCPHeader {
    public short head_flag = 0x03D9;
    public int size;
    public int sequence;
    public int cmd_id;
    public short proto_ver;
    public byte client_type;
    public byte conn_type;
    public byte packet_type;
    public byte mgs_type;
    public byte crypt_type;
    public long source;
    public long target;
    public short send_target_num;
    public long reserved;

}
