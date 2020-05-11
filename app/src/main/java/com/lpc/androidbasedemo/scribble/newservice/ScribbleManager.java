package com.lpc.androidbasedemo.scribble.newservice;

import android.content.Context;

import com.lpc.androidbasedemo.scribble.ScribbleView;
import com.lpc.androidbasedemo.scribble.entity.ScribbleConfig;
import com.lpc.androidbasedemo.scribble.utils.PageData;
import com.lpc.androidbasedemo.scribble.utils.RoomData;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzhiyuan
 * @date 2016/12/6
 * desc:
 */
public class ScribbleManager {
    private static ScribbleManager sInstance;
    private Context context;
    private RoomData roomData;
    private PageData pageData;
    private PageManager newPageManager;
    private PageManager oldPageManager;
    private boolean isNew = true;
    private ScribbleConfig scribbleConfig = new ScribbleConfig();
    /**
     * 记录用户ID和用户名称的对应关系
     */
    private Map<String, String> mUserId2UserNameMap = new HashMap<>();

    private RoomService roomService;
    private RoomService oldRoomService;

    private ScribbleManager() {
        roomData = new RoomData();
        pageData = new PageData();
        if (isNew) {
            roomService = new RoomService();
        } else {
            oldRoomService = new RoomService();
        }
    }

    public Context getContext() {
        return context;
    }

    /**
     * 初始化涂鸦service
     *
     * @param context
     * @param config
     */
    public void initService(Context context, ScribbleConfig config) {
        this.context = context.getApplicationContext();
        setIsNew(config.isNewService());
        scribbleConfig = config;
        if (config.isNewService()) {
            roomService.connect(config.getServiceUrl(), config.getServicePort(),
                    config.getRoomId(), config.getRole(), config.getRoomType(), config.getUserId());
        } else {
            oldRoomService.connect(config.getServiceUrl(), config.getServicePort(),
                    config.getRoomId(), config.getRole(), config.getRoomType(), config.getUserId());
        }
    }

    public RoomService getNewRoomService() {
        return roomService;
    }

    public RoomService getOldRoomService() {
        return oldRoomService;
    }

    public PageManager getNewPageManager() {
        return newPageManager;
    }

    public PageManager getOldPageManager() {
        return oldPageManager;
    }


    public void addUser(String userId, String userName) {
        mUserId2UserNameMap.put(userId, userName);
    }

    public Map<String, String> getUserId2UserNameMap() {
        return mUserId2UserNameMap;
    }

    public RoomData getRoomData() {
        return roomData;
    }

    public PageData getPageData() {
        return pageData;
    }

    public static ScribbleManager getsInstance() {
        if (sInstance == null) {
            synchronized (ScribbleManager.class) {
                if (sInstance == null) {
                    sInstance = new ScribbleManager();
                }
            }
        }
        return sInstance;
    }

    public int getClassType() {
        return scribbleConfig.getRoomType();
    }

    /**
     * 打开文档
     *
     * @param scribbleView
     * @param docId
     */
    public void openDoc(ScribbleView scribbleView, String docId) {
        WeakReference<ScribbleView> scribbleViewWeakReference = new WeakReference<>(scribbleView);
        if (isNew) {
            newPageManager = new PageManager(scribbleViewWeakReference.get());
        } else {
            oldPageManager = new PageManager(scribbleViewWeakReference.get());
        }
        roomData.setPageTypeId(docId);
        scribbleView.setTop(0);
        scribbleView.setClassType(scribbleConfig.getRoomType());
    }

    /**
     * 打开文档某一页
     *
     * @param docId
     * @param pageIndex
     */
    public void showPage(String docId, int pageIndex) {
        if (isNew) {
            getNewPageManager().showPage(docId, pageIndex);
        } else {
            getOldPageManager().showPage(docId, pageIndex);
        }
    }

    public String getRoomId() {
        return scribbleConfig.getRoomId();
    }

    public int getUserId() {
        return scribbleConfig.getUserId();
    }

    public String getUserName() {
        return scribbleConfig.getUserName();
    }

    public void release() {
        if (newPageManager != null) {
            newPageManager.release();
        }
        if (oldPageManager != null) {
            oldPageManager.release();
        }
        if (roomService != null) {
            roomService.release();
        }
        if (oldRoomService != null) {
            oldRoomService.release();
        }
        roomService = null;
        oldRoomService = null;
        newPageManager = null;
        sInstance = null;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
        if (this.isNew) {
            if (roomService == null) {
                roomService = new RoomService();
            }
        } else {
            if (oldRoomService == null) {
                oldRoomService = new RoomService();
            }
        }
    }

    public boolean isNew() {
        return this.isNew;
    }
}
