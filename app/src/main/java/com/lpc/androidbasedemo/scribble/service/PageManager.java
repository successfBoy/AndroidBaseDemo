package com.lpc.androidbasedemo.scribble.service;

import android.util.Log;

import com.lpc.androidbasedemo.scribble.ScribbleView;
import com.lpc.androidbasedemo.scribble.entity.BrushData;
import com.lpc.androidbasedemo.scribble.entity.CmdData;
import com.lpc.androidbasedemo.scribble.entity.Message;
import com.lpc.androidbasedemo.scribble.newservice.ScribbleManager;
import com.lpc.androidbasedemo.scribble.utils.PageRender;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * @author wangzhiyuan
 * @date 2016/12/7
 * desc:
 */

public class PageManager {
    private static final String TAG = "PageManager";
    private ScribbleView scribbleView;
    private HashMap<String, PageRender> pageArray;

    public PageManager(ScribbleView scribbleView) {
        this.scribbleView = scribbleView;
        pageArray = new HashMap<>();
    }

    public PageRender getPageRender(String docid, int pageid) {
        String key = buildDocIdKey(docid, pageid);
        PageRender render = pageArray.get(key);
        if (render == null) {
            createPage(docid, pageid);
            render = pageArray.get(key);
        }
        return render;
    }

    private void createPage(String docid, int pageid) {
        PageRender render = new PageRender(scribbleView);
        String key = buildDocIdKey(docid, pageid);
        ScribbleManager.getsInstance().getRoomData().setPageTypeId(docid);
        ScribbleManager.getsInstance().getRoomData().setPageId(pageid);
        pageArray.put(key, render);
        ScribbleManager.getsInstance().getOldRoomService().requestPatchedMouseMsg(docid, pageid);
    }

    public void showPage(String docid, int pageid) {
        int prePageId = ScribbleManager.getsInstance().getRoomData().getPageId();
        String preDocID = ScribbleManager.getsInstance().getRoomData().getPageTypeId();
        ScribbleManager.getsInstance().getRoomData().setPageId(pageid);
        ScribbleManager.getsInstance().getRoomData().setPageTypeId(docid);
        scribbleView.onPauseDraw(preDocID, prePageId);
        PageRender pageRender = getPageRender(docid, pageid);
        pageRender.drawAction(true);
        Logger.d(TAG, "showPage: current index========>>" + pageid + " pre index======>>" + prePageId);
    }

    public void dispatchBrushList(List<BrushData> brushDataList) {
        //静态涂鸦清空缓存数据
        boolean isclear = false;
        for (BrushData brushData : brushDataList) {
            String key = buildDocIdKey(brushData.getM_pageTypeId(), brushData.getM_pageId());
            PageRender render = pageArray.get(key);
            if (render != null) {
                if (!isclear) {
                    render.reset();
                    isclear = true;
                }
                render.dispatchBrush(brushData,true);
            } else {
                Log.d(TAG, "dispatchBrushList: render is " + render + "=======" + brushData.getM_pageId());
            }
        }
        Log.d(TAG, "dispatchBrushList: " + brushDataList.toString());


    }

    public void dispatchBrush(BrushData brushData) {
        String key = buildDocIdKey(brushData.getM_pageTypeId(), brushData.getM_pageId());
        PageRender render = pageArray.get(key);
        if (render != null) {
            render.dispatchBrush(brushData,false);
        } else {
            Log.e(TAG, "dispatchBrush render is null");
        }
        Log.d(TAG, "dispatchBrush: ======>" + brushData.toString());
    }

    public void onMouseMoveData(Message message) {
        int pageid = ScribbleManager.getsInstance().getRoomData().getPageId();
        String docID = ScribbleManager.getsInstance().getRoomData().getPageTypeId();
        PageRender render = getPageRender(docID, pageid);
        if (render != null) {
            render.onMouseMoveData(message);
        }
    }

    public void dispatchCmd(CmdData cmdData) {
        int pageId = ScribbleManager.getsInstance().getRoomData().getPageId();
        String docID = ScribbleManager.getsInstance().getRoomData().getPageTypeId();
        String key = buildDocIdKey(docID, pageId);
        PageRender render = pageArray.get(key);
        switch (cmdData.getM_cmd()) {
            case CMD_DRAW:
            case CMD_DELETE:
            case CMD_CLEAR_PAGE:
            case CMD_MOVE:
            case CMD_ZOOM: {
                if (render != null) {
                    render.dispatchCmd(cmdData);
                }
            }
            break;
            case CMD_SHOWPAGE:
                break;
            default:
                break;
        }

    }

    public void clear() {
        Logger.d("Clear scribble Data!");
        pageArray.clear();

    }

    public void release() {
        clear();
        scribbleView = null;
    }

    private String buildDocIdKey(String docID, int pageID) {
        return docID + ":" + pageID;
    }
}
