package com.lpc.androidbasedemo.scribble.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangzhiyuan on 2016/11/21.
 * desc:
 */

public enum BrushState {
    BRUSH_INVILADE(0),
    BRUSH_START(1),
    BRUSH_PROCESS(2),
    BRUSH_OVER(4),
    BRUSH_ALL(7);

    private static final Map<Integer, BrushState> MY_MAP = new HashMap<>();

    static {
        // populating the map
        for (BrushState myEnum : values()) {
            MY_MAP.put(myEnum.getValue(), myEnum);
        }
    }

    private int value;

    BrushState(int idx) {
        this.value = idx;
    }

    public int getValue() {
        return value;
    }

    public static BrushState getByValue(int value) {
        return MY_MAP.get(value);
    }

}