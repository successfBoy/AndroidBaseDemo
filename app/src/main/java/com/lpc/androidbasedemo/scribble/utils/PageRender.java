package com.lpc.androidbasedemo.scribble.utils;

import android.text.TextUtils;
import android.util.Log;

import com.lpc.androidbasedemo.scribble.ScribbleView;
import com.lpc.androidbasedemo.scribble.entity.Action;
import com.lpc.androidbasedemo.scribble.entity.BrushData;
import com.lpc.androidbasedemo.scribble.entity.CmdData;
import com.lpc.androidbasedemo.scribble.entity.Message;
import com.lpc.androidbasedemo.scribble.entity.MyLaster;
import com.lpc.androidbasedemo.scribble.entity.MyPenIndicator;
import com.lpc.androidbasedemo.scribble.entity.Point;
import com.lpc.androidbasedemo.scribble.entity.ScribbleMouseMoveMsgNotify;
import com.lpc.androidbasedemo.scribble.newservice.ScribbleManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.lpc.androidbasedemo.scribble.utils.BrushCmd.CMD_CLEAR_PAGE;
import static com.lpc.androidbasedemo.scribble.utils.BrushCmd.CMD_DELETE;
import static com.lpc.androidbasedemo.scribble.utils.BrushCmd.CMD_DRAW;
import static com.lpc.androidbasedemo.scribble.utils.BrushCmd.CMD_MOVE;
import static com.lpc.androidbasedemo.scribble.utils.BrushCmd.CMD_ZOOM;


/**
 * @author wangzhiyuan
 * @date 2016/12/6
 * desc:
 */

public class PageRender {

    private ScribbleView scribbleView;
    private List<Action> mActions;
    private MyLaster myLaster;
    private MyPenIndicator myPenIndicator;

    public PageRender(ScribbleView scribbleView) {
        this.scribbleView = scribbleView;
        mActions = new CopyOnWriteArrayList<>();
        scribbleView.setmActions(mActions);
        myLaster = new MyLaster();
        myPenIndicator = new MyPenIndicator();

    }

    public List<Action> getmActions() {
        return mActions;
    }

    public void dispatchCmd(CmdData cmdData) {
        switch (cmdData.getM_cmd()) {
            case CMD_DRAW:
            case CMD_DELETE:
                drawStrokeForRemote(cmdData);
                break;
            case CMD_CLEAR_PAGE:
                clearStrokeForRemote(cmdData);
                break;
            case CMD_MOVE:
            case CMD_ZOOM:
                replaceStroke(cmdData);
                break;
            default:
                break;
        }
    }

    private void drawStrokeForRemote(CmdData cmdData) {
        if (mActions != null && mActions.size() > 0) {
            for (Action a : mActions) {
                if (a == null) {
                    continue;
                }
                for (int id : cmdData.getM_strokeIds()) {
                    BrushData brushData = a.getBrushData();
                    if (brushData != null && a.isRemote() && brushData.getM_id() == id) {
                        if (cmdData.getM_cmd() == CMD_DELETE) {
                            int m = brushData.getM_showFlag();
                            if (cmdData.getM_showFlag(eBrushDataShowFlag.BD_SHOWFLAG_DELETE)) {
                                m |= eBrushDataShowFlag.BD_SHOWFLAG_DELETE.getIndex();
                                brushData.setM_showFlag((byte) m);

                            } else {
                                m &= (~eBrushDataShowFlag.BD_SHOWFLAG_DELETE.getIndex());

                                brushData.setM_showFlag((byte) m);
                            }
                        } else {
                            brushData.setM_showFlag(cmdData.getM_showflag());

                        }
                    }
                }
            }
            drawAction(false);
        }
    }

    private void clearStrokeForRemote(CmdData cmdData) {
        for (int id : cmdData.getM_strokeIds()) {
            for (Action a : mActions) {
                if (a == null) {
                    continue;
                }
                BrushData brushData = a.getBrushData();
                if (brushData != null && a.isRemote() && brushData.getM_id() == id) {
                    int m = brushData.getM_showFlag();
                    if (cmdData.getM_showFlag(eBrushDataShowFlag.BD_SHOWFLAG_CLEERSCREEN)) {
                        m |= eBrushDataShowFlag.BD_SHOWFLAG_CLEERSCREEN.getIndex();
                        brushData.setM_showFlag((byte) m);

                    } else {
                        m &= (~eBrushDataShowFlag.BD_SHOWFLAG_CLEERSCREEN.getIndex());

                        brushData.setM_showFlag((byte) m);
                    }
                }
            }
        }
        drawAction(false);

    }

    public void replaceStroke(CmdData cmdData) {

        for (BrushData brushData : cmdData.getM_brushDatas()) {
            for (Action a : mActions) {
                if (a == null) {
                    continue;
                }
                if (a.getBrushData() != null && a.isRemote() && a.getBrushData().getM_id() == brushData.getM_id()) {
                    a.setBrushData(brushData).updateAction();
                    break;
                }


            }
        }
        drawAction(false);

    }

    public void dispatchBrush(BrushData m_stroke,boolean isStatic) {
        Log.d("PageRender", "dispatchBrush--->M_status-->" + m_stroke.toString());
        switch (m_stroke.getM_status()) {
            case BRUSH_START:
            case BRUSH_ALL:
                Action action = ActionFactory.createAction(m_stroke);
                if (action == null) {
                    return;
                }
                action.setScribbleView(scribbleView).updateAction();
                addActions(action);
                break;
            case BRUSH_PROCESS:
            case BRUSH_OVER:
                compoundStroke(m_stroke);
                break;
            default:
                break;
        }

        drawAction(isStatic);

    }

    private void addActions(Action action) {
        if (action == null || action.getBrushData() == null) {
            return;
        }

        for (Action a : mActions) {
            if (a == null) {
                continue;
            }
            if (a.getBrushData() != null && a.getBrushData().getM_id() == action.getBrushData().getM_id()
                    && a.getBrushData().getM_userId() == action.getBrushData().getM_userId()) {
                a.setBrushData(action.getBrushData());
                return;
            }
        }
        mActions.add(action);
    }

    private void compoundStroke(BrushData m_stroke) {
        for (Action a : mActions) {
            if (a == null) {
                continue;
            }
            if (a.getBrushData() != null && a.isRemote() && a.getBrushData().getM_id() == m_stroke.getM_id()
                    && a.getBrushData().getM_userId() == m_stroke.getM_userId()) {
                //TODO:临时处理文字涂鸦变化的是内容
                if (m_stroke.getM_pen().getType() == StrokeType.ST_TEXT) {
                    a.setBrushData(m_stroke).updateAction();
                    break;
                }
                for (Point p : m_stroke.getM_points()) {
                    if (!a.getBrushData().getM_points().contains(p)) {
                        a.getBrushData().getM_points().add(p);
                        a.move(p.getX(), p.getY());
                    }

                }
                break;
            }
        }
    }

    public void drawAction(boolean isStatic) {
        scribbleView.setmActions(mActions);
        scribbleView.drawAction(isStatic);
    }

    public void onMouseMoveData(Message message) {
        ScribbleMouseMoveMsgNotify mouseMoveMsgNotify = (ScribbleMouseMoveMsgNotify) message;
        if (mouseMoveMsgNotify != null && mouseMoveMsgNotify.getData() != null) {
            String userName = mouseMoveMsgNotify.getData().getUsename();
            if (!TextUtils.isEmpty(userName)) {
                ScribbleManager.getsInstance().addUser(mouseMoveMsgNotify.getUserid() + "", userName);
            }
            if (mouseMoveMsgNotify.getData().getStroketype() == StrokeType.ST_LASER) {
                mActions.add(myLaster);
                myLaster.setScribbleView(scribbleView).setMouseMoveData(mouseMoveMsgNotify.getData()).updateAction();
                drawAction(false);
                mActions.remove(myLaster);
            }
        }
    }

    public void reset() {
        mActions.clear();
    }
}
