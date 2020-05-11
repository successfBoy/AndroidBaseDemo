package com.lpc.androidbasedemo.scribble.newservice;

import android.util.Log;

import com.lpc.androidbasedemo.scribble.ScribbleView;
import com.lpc.androidbasedemo.scribble.entity.BrushData;
import com.lpc.androidbasedemo.scribble.entity.CmdData;
import com.lpc.androidbasedemo.scribble.entity.Message;
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

    public PageRender getPageRender(String docID, int pageid) {
        PageRender render = pageArray.get(docID + ":" + pageid);
        if (render == null) {
            createPage(docID, pageid);
            render = pageArray.get(buildDocIdKey(docID, pageid));
        }
        return render;
    }

    private void createPage(String docID, int pageid) {
        Logger.d(TAG, "createPage pageId:" + pageid + " docID:" + pageid);
        PageRender render = new PageRender(scribbleView);
        pageArray.put(buildDocIdKey(docID, pageid), render);
    }

    public void showPage(String docid, int pageid) {
        if (ScribbleManager.getsInstance().isNew()) {
            ScribbleManager.getsInstance().getNewRoomService().requestStaticScribbleData(docid, pageid);
        } else {
            ScribbleManager.getsInstance().getOldRoomService().requestPatchedMouseMsg(docid, pageid);
        }
        int prePageId = ScribbleManager.getsInstance().getRoomData().getPageId();
        String preDocID = ScribbleManager.getsInstance().getRoomData().getPageTypeId();
        scribbleView.onPauseDraw(preDocID, prePageId);
        ScribbleManager.getsInstance().getRoomData().setPageId(pageid);
        ScribbleManager.getsInstance().getRoomData().setPageTypeId(docid);
        PageRender pageRender = getPageRender(docid, pageid);
        pageRender.drawAction(true);
        Logger.d(TAG + "%s", "showPage: current index========>>" + pageid + " pre index======>>" + prePageId + " docid:" + docid);
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
        }
        Log.d(TAG, "dispatchBrush: ======>" + brushData.toString());
    }

    public void onMouseMoveData(Message message) {
        int pageid = ScribbleManager.getsInstance().getRoomData().getPageId();
        String docId = ScribbleManager.getsInstance().getRoomData().getPageTypeId();
        PageRender render = getPageRender(docId, pageid);
        if (render != null) {
            render.onMouseMoveData(message);
        }
        Log.d(TAG, "onMouseMoveData:  " + message);
    }

    public void dispatchCmd(CmdData cmdData) {
        Logger.d("dispatchCmd pageTypeID:" + cmdData.getM_pageTypeId());
        String key = cmdData.getM_pageTypeId();
        PageRender render = pageArray.get(key);
        int pageid = cmdData.getM_pageId();
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

    public ScribbleView getScribbleView() {
        return this.scribbleView;
    }

    private String buildDocIdKey(String docID, int pageID) {
        return docID + ":" + pageID;
    }
}
