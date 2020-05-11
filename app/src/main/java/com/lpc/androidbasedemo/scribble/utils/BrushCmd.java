package com.lpc.androidbasedemo.scribble.utils;

/**
 * Created by wangzhiyuan on 2016/11/21.
 * desc:
 */

public enum BrushCmd {
    CMD_SUBBLE,//涂鸦

    CMD_DRAW,// 画

    CMD_MOVE,
    CMD_ZOOM,
    CMD_DELETE,

    CMD_CLEAR_PAGE,//清屏

    CMD_CLEAR_STROKE,

    CMD_SHOWPAGE,//显示某页
    CMD_HIDEPAGE,
    CMD_DESTORY_ALL,//清除所有页面
    //CMD_CREATE_PAGE,//创建page(@Deperacate)
    CMD_DESTORY_PAGE,
    CMD_SHOW_AREA,//切换涂鸦
    CMD_DESTORY_AREA,
    //CMD_HIDE_LASER,//隐藏激光笔(激光笔暂时走截图)

    CMD_INVALID
}
