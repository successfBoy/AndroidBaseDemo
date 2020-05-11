package com.lpc.androidbasedemo.scribble.utils;

import com.lpc.androidbasedemo.scribble.entity.Action;
import com.lpc.androidbasedemo.scribble.entity.BrushData;
import com.lpc.androidbasedemo.scribble.entity.MyCircle;
import com.lpc.androidbasedemo.scribble.entity.MyImage;
import com.lpc.androidbasedemo.scribble.entity.MyLine;
import com.lpc.androidbasedemo.scribble.entity.MyLineArrow;
import com.lpc.androidbasedemo.scribble.entity.MyPath;
import com.lpc.androidbasedemo.scribble.entity.MyRect;
import com.lpc.androidbasedemo.scribble.entity.MyText;
import com.lpc.androidbasedemo.scribble.entity.MyTriangle;

/**
 * Created by wangzhiyuan on 2016/11/21.
 * desc:
 */

public class ActionFactory {
    public static Action createAction(BrushData brushData) {
        switch (brushData.getM_pen().getType()) {
            case ST_IMAGE:
                return new MyImage(brushData);
            case ST_PEN:
                return new MyPath(brushData);
            case ST_LINE:
                return new MyLine(brushData);
            case ST_LINEARROW:
                return new MyLineArrow(brushData);
            case ST_ELLIPSE:
                return new MyCircle(brushData);
            case ST_RECT:
                return new MyRect(brushData);
            case ST_TRIANGLE:
                return new MyTriangle(brushData);
            case ST_TEXT:
                return new MyText(brushData);
            default:
                return null;
        }
    }
}
