package com.lpc.androidbasedemo.scribble.utils;

/**
 * Created by wangzhiyuan on 2016/11/29.
 * desc:
 */

public enum eBrushDataShowFlag {
    BD_SHOWFLAG_INVALID(0),
    BD_SHOWFLAG_DRAW(1 << 0),//显示
    BD_SHOWFLAG_DELETE(1 << 1),//delete状态 不显示
    BD_SHOWFLAG_CLEERSCREEN(1 << 2);//清屏状态  不显示


    private int index;

    eBrushDataShowFlag(int idx) {
        this.index = idx;
    }

    public int getIndex() {
        return index;
    }
}
