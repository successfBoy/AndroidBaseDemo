package com.lpc.androidbasedemo.scribble.utils;

/**
 * Created by wangzhiyuan on 2018/1/31.
 * desc:
 */

public interface Constants {
    interface ScribbleMessage{
        int MESSAGE_ACC_CONNECT_INFO                = 0x00050100;//获取接入服务信息
        int MESSAGE_LOGIN_CLIENT_HEARTBEAT          = 0x00060010;//客户端发送心跳包命令

        int MESSAGE_ACC_CONNECT                     = 0x00060001;//客户端接入
        int MESSAGE_LOGIN                           = 0x00060003;//用户登录
        int MESSAGE_LOGIN_COMPLETE                  = 0x00060005;//用户登录完毕
        int MESSAGE_LOGOUT                          = 0x00060006;//用户登出
        int MESSAGE_LOGOUT_FORCED                   = 0x00060007;//强制用户下线

        int MESSAGE_CLASS_ENTER                     = 0x00070001;//进教室
        int MESSAGE_CLASS_ENTER_COMPLETE            = 0x00070002;//进入教室完成
        int MESSAGE_CLASS_GET_LIST                  = 0x00070004;//请求教室列表
        int MESSAGE_CLASS_LEAVE                     = 0x00070005;//离开教室
        int MESSAGE_CLASS_LEAVE_FORCED_NOTI         = 0x00070006;//被强制离开教室通知

        int MESSAGE_CLASS_CLOSED_NOTI               = 0x00130015;//教室关闭通知
        int MESSAGE_CLASS_CHAT_MSG                  = 0x00130016;//聊天消息



        int MESSAGE_DOODLE_DYNAMIC                  = 0x00080001; //发送动态涂鸦
        int MESSAGE_DOODLE_DYNAMIC_SYNC             = 0x00080002; //同步动态涂鸦数据
        int MESSAGE_DOODLE_STATIC                   = 0x00080003; //获取静态涂鸦请求
        int MESSAGE_DOODLE_STATE_UPDATE             = 0x00080004; //更新涂鸦状态(撤销反撤销/清屏)
        int MESSAGE_DOODLE_STATE_UPDATE_SYNC        = 0x00080005; //同步涂鸦状态(撤销反撤销/清屏)
        int MESSAGE_DOODLE_MESSAGE_CHANGE_SYNC      = 0x00080007; //同步涂鸦消息(拖拽)
        int MESSAGE_DOODLE_MOUSEMOVE                = 0x00080010; //鼠标移动
        int MESSAGE_DOODLE_MOUSEMOVE_SYNC           = 0x00080011; //同步鼠标移动
    }
}
