package com.lpc.androidbasedemo.scribble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.scribble.entity.Action;
import com.lpc.androidbasedemo.scribble.entity.BrushData;
import com.lpc.androidbasedemo.scribble.entity.MyCircle;
import com.lpc.androidbasedemo.scribble.entity.MyFillCircle;
import com.lpc.androidbasedemo.scribble.entity.MyFillRect;
import com.lpc.androidbasedemo.scribble.entity.MyLine;
import com.lpc.androidbasedemo.scribble.entity.MyLineArrow;
import com.lpc.androidbasedemo.scribble.entity.MyPath;
import com.lpc.androidbasedemo.scribble.entity.MyPoint;
import com.lpc.androidbasedemo.scribble.entity.MyRect;
import com.lpc.androidbasedemo.scribble.entity.MyTriangle;
import com.lpc.androidbasedemo.scribble.entity.Point;
import com.lpc.androidbasedemo.scribble.entity.ScribbleMouseMoveMsgNotify;
import com.lpc.androidbasedemo.scribble.newservice.ScribbleManager;
import com.lpc.androidbasedemo.scribble.utils.BrushState;
import com.lpc.androidbasedemo.scribble.utils.DataStructureMapping;
import com.lpc.androidbasedemo.scribble.utils.PageRender;
import com.lpc.androidbasedemo.scribble.utils.StrokeType;
import com.lpc.androidbasedemo.scribble.utils.Utils;
import com.lpc.androidbasedemo.scribble.utils.eBrushDataShowFlag;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.view.Gravity.CENTER;
import static com.lpc.androidbasedemo.scribble.utils.StrokeType.ST_PEN;
import static com.lpc.androidbasedemo.scribble.utils.eBrushDataShowFlag.BD_SHOWFLAG_DELETE;
import static com.lpc.androidbasedemo.scribble.utils.eBrushDataShowFlag.BD_SHOWFLAG_DRAW;


/**
 * @author tal
 */
public class ScribbleView extends TextureView implements TextureView.SurfaceTextureListener {

    public static final int PAN_SIZE_BOLD = 8;
    public static final int PAN_SIZE_MIDDLE = 5;
    public static final int PAN_SIZE_THIN = 3;

    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;

    private BackOrForwardIsEnable mBackForwardListener;

    /**
     * 当前所选画笔的形状
     */
    private Action curAction = null;
    /**
     * 默认画笔为黑色
     */
    private int currentColor = Color.BLACK;
    /**
     * 画笔的粗细
     */
    private int currentSize = 3;

    private Paint mPaint;
    /**
     * 记录画笔的列表
     */
    private List<Action> mActions = new CopyOnWriteArrayList<>();
    /**
     * 记录用户ID和名称的对应关系
     */
    private Map<String, String> mUserNameCache;
    private int mClassType;
    private int myActionIndex = -1;
    private List<Action> redoActions;
    private Bitmap bmp;

    private ActionType type = ActionType.Path;
    private BrushData currentBrushData;
    private BrushData currentSendBrushData;
    private List<Point> currentSendPointList = new CopyOnWriteArrayList<>();
    private Disposable mDisposable;
    private boolean isAuthorize = true;
    private Pen mPen;
    private boolean isPauseDraw = false;
    private boolean isDrawing = false;

    private TextView mDrawUserTv;
    private int mScrollY;
    private ScribbleMouseMoveMsgNotify mMouseMoveMsgNotify;

    public ScribbleView(Context context) {
        super(context);
        init();
    }

    public ScribbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setmActions(List<Action> mActions) {
        this.mActions = mActions;
    }

    private void init() {
        this.setFocusable(true);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(currentSize);
        setOpaque(false);
        startSendTask();
        setSurfaceTextureListener(this);
        mUserNameCache = ScribbleManager.getsInstance().getUserId2UserNameMap();
        post(new Runnable() {
            @Override
            public void run() {
                mDrawUserTv = new TextView(getContext());
                mDrawUserTv.setBackground(getResources().getDrawable(R.drawable.doodle_draw_user_name_bg));
                mDrawUserTv.setTextSize(10);
                mDrawUserTv.setTextColor(Color.WHITE);
                mDrawUserTv.setGravity(CENTER);
                mDrawUserTv.setVisibility(GONE);
                mDrawUserTv.setSingleLine();
                mDrawUserTv.setPadding(Utils.dp2px(getContext(), 10), Utils.dp2px(getContext(), 5), Utils.dp2px(getContext(), 10), Utils.dp2px(getContext(), 5));
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ((ViewGroup) getParent()).addView(mDrawUserTv, layoutParams);
            }
        });
    }

    /**
     * 通知涂鸦课程类型，小班课需要显示用户名称
     *
     * @param type
     */
    public void setClassType(int type) {
        mClassType = type;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_CANCEL) {
            return false;
        }
        if (!isAuthorize) {
            //未授权不可画
//            return super.onTouchEvent(event);
            return false;
        }
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                // 记录按下时间
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startClickTime = System.currentTimeMillis();
                }

                if (event.getPointerId(event.getActionIndex()) == 0) {
                    float downX = touchX;
                    float downY = touchY;
                    if (event.getPointerId(event.getActionIndex()) == 0) {
                        int indexDown = event.findPointerIndex(0);
                        downX = event.getX(indexDown);
                        downY = event.getY(indexDown);
                    }
                    Point startPoint = new Point();
                    startPoint.setX((short) downX);
                    startPoint.setY((short) downY);
                    startPoint.setInterval((short) 0);
                    currentSendPointList.add(startPoint);
                    int usrDrawId = Utils.getUniqueNumber();
                    currentBrushData = DataStructureMapping.generateBrushData(BrushState.BRUSH_START, usrDrawId, getWidth(), getHeight(), currentSendPointList, ST_PEN, mPen);

                    //当为小班时发送消息需要显示用户名
                    if (ScribbleManager.getsInstance().getClassType() == 7) {
                        mMouseMoveMsgNotify = DataStructureMapping.generateMouseMoveData(ScribbleManager.getsInstance().getUserId(),
                                ScribbleManager.getsInstance().getUserName(), ScribbleManager.getsInstance().getRoomData().getPageTypeId(),
                                ST_PEN, startPoint, getWidth(), getHeight(), true);
                        DataStructureMapping.sendMouseMoveData(mMouseMoveMsgNotify);
                    }

                    currentSendBrushData = DataStructureMapping.generateBrushData(BrushState.BRUSH_START, usrDrawId, getWidth(), getHeight(), currentSendPointList, ST_PEN, mPen);
                    DataStructureMapping.sendBrushData(currentSendBrushData);
                    currentSendPointList.clear();
                    currentSendBrushData.clearPointData();
                    setCurAction(downX, downY, currentBrushData);
                    curAction.setScribbleView(this);
                    isDrawing = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = 0;
                float moveY = 0;
                try {
                    int index = event.findPointerIndex(0);
                    moveX = event.getX(index);
                    moveY = event.getY(index);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                if (isPauseDraw) {
                    currentBrushData.setM_status(BrushState.BRUSH_OVER);
                    currentSendBrushData.setM_status(BrushState.BRUSH_OVER);
                    currentSendBrushData.clearPointData();
                    currentSendPointList.clear();
                    DataStructureMapping.sendBrushData(currentSendBrushData);
                    break;
                }
                if (null == curAction) {
                    break;
                }
                Canvas canvas = lockCanvas();
                if (canvas == null) {
                    Logger.e("onTouchEvent: canvas is NULL");
                    return true;
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                for (int i = 0; i < mActions.size(); i++) {
                    Action a = mActions.get(i);
                    if (null == a) {
                        continue;
                    }
                    if (a.getBrushData() != null && (!a.isRemote() || !a.getBrushData().getM_showFlag(BD_SHOWFLAG_DELETE)
                            && !a.getBrushData().getM_showFlag(eBrushDataShowFlag.BD_SHOWFLAG_CLEERSCREEN)
                            && a.getBrushData().getM_showFlag(BD_SHOWFLAG_DRAW))) {
                        a.draw(canvas);
                    }
                }
                Point point = new Point();
                point.setX((short) moveX);
                point.setY((short) moveY);
                point.setInterval((short) 0);
                currentBrushData.setM_status(BrushState.BRUSH_PROCESS);
                currentSendBrushData.setM_status(BrushState.BRUSH_PROCESS);
                currentBrushData.addPoint(point);
                currentSendPointList.add(point);
                curAction.move(moveX, moveY);
                curAction.draw(canvas);
                isDrawing = true;
                unlockCanvasAndPost(canvas);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                // 处理点击事件
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    long clickTime = System.currentTimeMillis() - startClickTime;
                    if (clickTime < MAX_CLICK_DURATION && isClickable()) {
                        performClick();
                    }
                }

                float upX = touchX;
                float upY = touchY;
                if (event.getPointerId(event.getActionIndex()) == 0) {
                    int indexUp = event.findPointerIndex(0);
                    upX = event.getX(indexUp);
                    upY = event.getY(indexUp);
                }
                Point endPoint = new Point();
                endPoint.setX((short) upX);
                endPoint.setY((short) upY);
                endPoint.setInterval((short) 0);
                currentSendPointList.add(endPoint);
                if (!isPauseDraw) {
                    mActions.add(curAction);
                    if (mBackForwardListener != null) {
                        mBackForwardListener.isBackEnable(true);
                    }
                }
                curAction = null;
                currentBrushData.setM_status(BrushState.BRUSH_OVER);
                currentSendBrushData.setM_status(BrushState.BRUSH_OVER);
                currentSendBrushData.clearPointData();
                if (!isPauseDraw) {
                    currentSendBrushData.addPoint(endPoint);
                    DataStructureMapping.sendBrushData(currentSendBrushData);
                }
                currentSendPointList.clear();
                isPauseDraw = false;
                isDrawing = false;
                break;
            default:
                break;
        }
        return true;
    }

    // 得到当前画笔的类型，并进行实例
    public void setCurAction(float x, float y, BrushData brushData) {
        switch (type) {
            case Point:
                curAction = new MyPoint(x, y, currentColor);
                break;
            case Path:
                curAction = new MyPath(brushData, x, y);
                break;
            case Line:
                curAction = new MyLine(x, y, currentSize, currentColor);
                break;
            case Line_Arrow:
                curAction = new MyLineArrow(x, y, currentSize, currentColor);
                break;
            case Rect:
                curAction = new MyRect(x, y, currentSize, currentColor);
                break;
            case Circle:
                curAction = new MyCircle(x, y, currentSize, currentColor);
                break;
            case FillecRect:
                curAction = new MyFillRect(x, y, currentSize, currentColor);
                break;
            case FilledCircle:
                curAction = new MyFillCircle(x, y, currentSize, currentColor);
                break;
            case Triangle:
                curAction = new MyTriangle(x, y, currentSize, currentColor);
                break;
            default:
                break;
        }
    }

    /**
     * 设置画笔的颜色
     *
     * @param color
     */
    public void setColor(String color) {
        currentColor = Color.parseColor(color);
    }

    /**
     * 设置画笔的粗细
     *
     * @param size
     */
    public void setSize(int size) {
        currentSize = size;
    }

    /**
     * 设置当前画笔的形状
     *
     * @param type
     */
    public void setType(ActionType type) {
        this.type = type;
    }

    /**
     * 获取画布的截图
     *
     * @return
     */
    @Override
    public Bitmap getBitmap() {
        bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        doDraw(canvas);
        return bmp;
    }

    public void doDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        for (Action a : mActions) {
            if (a != null) {
                a.draw(canvas);
            }
        }
        canvas.drawBitmap(bmp, 0, 0, mPaint);
    }

    public void clearPage() {
        redoActions.addAll(mActions);
        mActions.clear();
        drawAction(true);
    }

    public void onPauseDraw(String preDocID, int prePageID) {
        if (isDrawing) {
            this.isPauseDraw = true;
        }
        PageRender pageRender;
        boolean isNew = ScribbleManager.getsInstance().isNew();
        if (isNew) {
            pageRender = ScribbleManager.getsInstance().getNewPageManager().getPageRender(preDocID, prePageID);
        } else {
            pageRender = ScribbleManager.getsInstance().getOldPageManager().getPageRender(preDocID, prePageID);
        }
        if (pageRender != null && curAction != null) {
            List<Action> actions = pageRender.getmActions();
            if (actions != null) {
                curAction.getBrushData().setM_pageTypeId(preDocID);
                curAction.getBrushData().setM_pageId(prePageID);
                actions.add(curAction);
            }
        }
        String docID = ScribbleManager.getsInstance().getRoomData().getPageTypeId();
        int pageID = ScribbleManager.getsInstance().getRoomData().getPageId();
        if (currentBrushData != null) {
            currentBrushData.setM_pageTypeId(docID);
            currentBrushData.setM_pageId(pageID);
        }
        if (currentSendBrushData != null) {
            currentSendBrushData.setM_pageTypeId(docID);
            currentBrushData.setM_pageId(pageID);
        }
    }

    private Runnable DrawUserNameRunnable = new Runnable() {
        @Override
        public void run() {
            if (mDrawUserTv != null) {
                mDrawUserTv.setVisibility(GONE);
            }
        }
    };

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        redoActions = new ArrayList<>();
        for (Action a : mActions) {
            if (a != null) {
                a.updateAction();
            }
        }
        drawAction(true);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        for (Action a : mActions) {
            if (a != null) {
                a.updateAction();
            }
        }
        drawAction(true);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    public enum ActionType {
        Point, Path, Line, Rect, Circle, FillecRect, FilledCircle, Eraser, Triangle, Line_Arrow
    }

    private void startSendTask() {
        mDisposable = Flowable.interval(100, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        if (curAction != null && !currentSendPointList.isEmpty() &&
                                currentSendBrushData.getM_status() == BrushState.BRUSH_PROCESS) {
                            currentSendBrushData.clearPointData();
                            currentSendBrushData.addPointList(currentSendPointList);
                            currentSendPointList.clear();
                            DataStructureMapping.sendBrushData(currentSendBrushData);
                            currentSendBrushData.clearPointData();
                        }
                    }
                });
    }

    public void updateM_id(int m_id) {//该涂鸦在房间内唯一的ID
        if (currentBrushData != null && currentBrushData.getM_status() == BrushState.BRUSH_OVER) {
            currentBrushData.setM_id(m_id);
        }
        if (currentSendBrushData != null && currentSendBrushData.getM_status() == BrushState.BRUSH_OVER) {
            currentSendBrushData.setM_id(m_id);
        }
    }

    public void setCurrentBrushDataUseDrawID(int usrDrawId) {
        if (currentSendBrushData != null) {
            currentSendBrushData.setUser_draw_id(usrDrawId);
        }
        if (currentBrushData != null) {
            currentBrushData.setUser_draw_id(usrDrawId);
        }
    }

    public interface BackOrForwardIsEnable {
        void isBackEnable(boolean isBackEnable);

        void isForwardEnable(boolean isForwardEnable);
    }

    public void setBackForwardListener(BackOrForwardIsEnable backForwardListener) {
        this.mBackForwardListener = backForwardListener;
    }

    public void setAuthorize(boolean isAuthorize) {
        this.isAuthorize = isAuthorize;
    }

    public void setPaintSize(int size) {
        if (mPen == null) {
            mPen = new Pen();
            mPen.setType(ST_PEN);
        }
        currentSize = size;
        mPen.setRude(currentSize);
        mPen.setColor(currentColor);
    }

    public void setPaintColor(String paintColor) {
        if (mPen == null) {
            mPen = new Pen();
            mPen.setType(ST_PEN);
        }
        currentColor = Color.parseColor(paintColor);
        mPen.setColor(currentColor);
        mPen.setRude(currentSize);
    }

    /**
     * 回退
     *
     * @return
     */
    public boolean back() {
//        if (!isAuthorize) {
//            return false;
//        }
        int index = mActions.size() - 1;
        if (index < 0) {
            return false;
        }
        if (mActions.get(index) == null) {
            return false;
        }
        int roomUserId;
        if (ScribbleManager.getsInstance().isNew()) {
            roomUserId = ScribbleManager.getsInstance().getNewRoomService().getUserId();
        } else {
            roomUserId = ScribbleManager.getsInstance().getOldRoomService().getUserId();
        }
        while (index >= 0 && mActions.get(index) != null &&
                (mActions.get(index).getBrushData().getM_userId() != roomUserId ||
                        (mActions.get(index).getBrushData().getM_userId() == roomUserId &&
                                !mActions.get(index).getBrushData().getM_showFlag(BD_SHOWFLAG_DRAW)))) {
            index--;
        }
        if (index < 0) {
            return false;
        }
        if (mActions.get(index) != null) {
            mActions.get(index).getBrushData().setM_showFlag((byte) BD_SHOWFLAG_DELETE.getIndex());
        }
        if (mBackForwardListener != null) {
            mBackForwardListener.isForwardEnable(true);
        }
        //发送消息
        if (mActions.get(index) != null) {
            DataStructureMapping.sendRedoCmd(mActions.get(index).getBrushData(), true);
        }
        myActionIndex = index;
        drawAction(true);
        return true;
    }

    public boolean forward() {
//        if (!isAuthorize) {
//            return false;
//        }
        int size = mActions.size();
        if (myActionIndex < 0 || myActionIndex >= size) {
            return false;
        }
        mActions.get(myActionIndex).getBrushData().setM_showFlag((byte) BD_SHOWFLAG_DRAW.getIndex());
        DataStructureMapping.sendRedoCmd(mActions.get(myActionIndex).getBrushData(), false);
        int roomUserId;
        if (ScribbleManager.getsInstance().isNew()) {
            roomUserId = ScribbleManager.getsInstance().getNewRoomService().getUserId();
        } else {
            roomUserId = ScribbleManager.getsInstance().getOldRoomService().getUserId();
        }
        while ((myActionIndex <= size - 1 &&
                mActions.get(myActionIndex).getBrushData().getM_userId() != roomUserId) ||
                (myActionIndex <= size - 1 && mActions.get(myActionIndex).getBrushData().getM_userId() == roomUserId &&
                        mActions.get(myActionIndex).getBrushData().getM_showFlag(BD_SHOWFLAG_DRAW))) {
            myActionIndex++;
        }
        if (myActionIndex <= size - 1) {
            if (mBackForwardListener != null) {
                mBackForwardListener.isForwardEnable(true);
            }
        } else {
            if (mBackForwardListener != null) {
                mBackForwardListener.isForwardEnable(false);
            }
        }
        drawAction(true);
        return true;
    }

    /**
     * 涂鸦在Y轴方向滚动
     *
     * @param scrollY
     */
    public void scrollY(int scrollY) {

    }

    public void drawAction(boolean isStatic) {
        if (getHandler() != null) {
            getHandler().removeCallbacks(DrawUserNameRunnable);
        }
        this.setVisibility(VISIBLE);
        Canvas canvas = lockCanvas();
        if (canvas == null) {
            Logger.e("drawAction: canvas is NULL");
            return;
        }
        int count = 0;
        int useId = ScribbleManager.getsInstance().isNew() ? ScribbleManager.getsInstance().getNewRoomService().getUserId() :
                ScribbleManager.getsInstance().getOldRoomService().getUserId();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        for (Action a : mActions) {
            if (a == null) {
                continue;
            }
            if (!a.isRemote()
                    || !a.getBrushData().getM_showFlag(BD_SHOWFLAG_DELETE)
                    && !a.getBrushData().getM_showFlag(eBrushDataShowFlag.BD_SHOWFLAG_CLEERSCREEN)
                    && a.getBrushData().getM_showFlag(BD_SHOWFLAG_DRAW)) {
                a.draw(canvas);
                if (!isStatic && mClassType == 7 && mDrawUserTv != null && a == mActions.get(mActions.size() - 1)) {
                    drawUserName(a);
                }
                if (a.getBrushData() != null
                        && a.getBrushData().getM_showFlag(BD_SHOWFLAG_DRAW)
                        && a.getBrushData().getM_userId() == useId) {
                    count++;
                }
            }
        }
        final boolean isCanBack = count > 0;
        this.post(new Runnable() {
            @Override
            public void run() {
                if (mBackForwardListener != null) {
                    mBackForwardListener.isBackEnable(isCanBack);
                    mBackForwardListener.isForwardEnable(myActionIndex >= 0 && myActionIndex <= mActions.size() - 1);
                }
            }
        });
        unlockCanvasAndPost(canvas);
    }

    /**
     * 显示用户名称
     *
     * @param action
     */
    private void drawUserName(final Action action) {
        try {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (action.getBrushData() == null) {
                        return;
                    }
                    String userName = mUserNameCache.get(String.valueOf(action.getBrushData().getM_userId()));
                    if (!TextUtils.isEmpty(userName)) {
                        mDrawUserTv.setText(userName);
                        boolean result = action.drawUserName(mDrawUserTv);
                        if (result) {
                            mDrawUserTv.setVisibility(VISIBLE);
                            getHandler().postDelayed(DrawUserNameRunnable, 2000);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
