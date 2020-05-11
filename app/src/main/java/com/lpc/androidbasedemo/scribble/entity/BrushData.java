package com.lpc.androidbasedemo.scribble.entity;


import com.koushikdutta.async.ByteBufferList;
import com.lpc.androidbasedemo.scribble.Pen;
import com.lpc.androidbasedemo.scribble.utils.BrushState;
import com.lpc.androidbasedemo.scribble.utils.StrokeType;
import com.lpc.androidbasedemo.scribble.utils.Utils;
import com.lpc.androidbasedemo.scribble.utils.eBrushDataShowFlag;
import com.orhanobut.logger.Logger;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTF16Bytes;
import static com.lpc.androidbasedemo.scribble.utils.Utils.getUTFStrFormBytes;
import static com.lpc.androidbasedemo.scribble.utils.Utils.intToBytes;
import static com.lpc.androidbasedemo.scribble.utils.Utils.shortToByteArray;

/**
 * Created by wangzhiyuan on 2016/11/16.
 * desc:
 */
public class BrushData extends Message {
    int user_draw_id;//返回的用户笔画ID
    int m_userId;
    String m_pageTypeId;

    int m_pageId;

    int m_id;//这个和userid唯一确定一个笔画

    BrushState m_status;//一笔状态

    short m_areaWidth;
    short m_areaHeight;
    short m_width;
    short m_height;
    Pen m_pen = new Pen();

    int m_pCount;//一笔的点个数
    List<Point> m_points = new CopyOnWriteArrayList<>();//所有点
    /*绘制相关*/
    int m_index;//当前绘制到那个点了
    int m_offset;

    byte m_showFlag;

    byte textFlag;

    String m_textureUrl;

    boolean m_remote;

    boolean m_uploadtexture;

    String text = "";
    String fontName;
    byte fontSize;


    public int getM_userId() {
        return m_userId;
    }

    public void setM_userId(int m_userId) {
        this.m_userId = m_userId;
    }

    public String getM_pageTypeId() {
        return m_pageTypeId;
    }

    public void setM_pageTypeId(String m_pageTypeId) {
        this.m_pageTypeId = m_pageTypeId;
    }

    public int getM_pageId() {
        return m_pageId;
    }

    public void setM_pageId(int m_pageId) {
        this.m_pageId = m_pageId;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public BrushState getM_status() {
        return m_status;
    }

    public void setM_status(BrushState m_status) {
        this.m_status = m_status;
    }

    public short getM_areaWidth() {
        return m_areaWidth;
    }

    public void setM_areaWidth(short m_areaWidth) {
        this.m_areaWidth = m_areaWidth;
    }

    public short getM_areaHeight() {
        return m_areaHeight;
    }

    public void setM_areaHeight(short m_areaHeight) {
        this.m_areaHeight = m_areaHeight;
    }

    public short getM_width() {
        return m_width;
    }

    public void setM_width(short m_width) {
        this.m_width = m_width;
    }

    public short getM_height() {
        return m_height;
    }

    public void setM_height(short m_height) {
        this.m_height = m_height;
    }

    public int getM_pCount() {
        return m_pCount;
    }

    public void setM_pCount(int m_pCount) {
        this.m_pCount = m_pCount;
    }

    public int getM_index() {
        return m_index;
    }

    public void setM_index(int m_index) {
        this.m_index = m_index;
    }

    public int getM_offset() {
        return m_offset;
    }

    public void setM_offset(int m_offset) {
        this.m_offset = m_offset;
    }

    public byte getM_showFlag() {
        return m_showFlag;
    }

    public boolean getM_showFlag(eBrushDataShowFlag showFlag) {// TODO: 2018/10/15 暂时把5当成4处理
        byte showFlagLocal = m_showFlag;
        if (5==m_showFlag){
            showFlagLocal = 4;
        }
        int m = showFlagLocal < 0 ? ~showFlagLocal : showFlagLocal;
        return (m & showFlag.getIndex()) != 0;
    }

    public void setM_showFlag(byte m_showFlag) {
        this.m_showFlag = m_showFlag;
    }

    public void setM_showFlag(eBrushDataShowFlag showFlag) {
        this.m_showFlag |= m_showFlag;
    }


    public String getM_textureUrl() {
        return m_textureUrl;
    }

    public void setM_textureUrl(String m_textureUrl) {
        this.m_textureUrl = m_textureUrl;
    }

    public boolean isM_remote() {
        return m_remote;
    }

    public void setM_remote(boolean m_remote) {
        this.m_remote = m_remote;
    }

    public boolean isM_uploadtexture() {
        return m_uploadtexture;
    }

    public void setM_uploadtexture(boolean m_uploadtexture) {
        this.m_uploadtexture = m_uploadtexture;
    }

    public Pen getM_pen() {
        return m_pen;
    }

    public void setM_pen(Pen m_pen) {
        this.m_pen = m_pen;
    }

    public List<Point> getM_points() {
        return m_points;
    }

    public void setM_points(List<Point> m_points) {
        this.m_points = m_points;
    }

    public byte getTextFlag() {
        return textFlag;
    }

    public void setTextFlag(byte textFlag) {
        this.textFlag = textFlag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public byte getFontSize() {
        return fontSize;
    }

    public void setFontSize(byte fontSize) {
        this.fontSize = fontSize;
    }

    public void setUser_draw_id(int user_draw_id){
        this.user_draw_id = user_draw_id;
    }
    public int getUserDrawId(){
        return this.user_draw_id;
    }
    public void addPointList(List<Point> pointList){
        if (pointList!=null){
            for (int i=0;i<pointList.size();i++){
                Point point = new Point();
                point.setX(pointList.get(i).getX());
                point.setY(pointList.get(i).getY());
                point.setInterval(pointList.get(i).getInterval());
                m_points.add(point);
            }
        }
        m_pCount = m_points.size();
    }
    public void clearPointData(){
        m_points.clear();
        m_pCount = 0;
    }
    public void addPoint(Point point){
        Point pointAdd = new Point();
        pointAdd.setInterval(point.getInterval());
        pointAdd.setX(point.getX());
        pointAdd.setY(point.getY());
        m_points.add(pointAdd);
        m_pCount = m_points.size();
    }

    @Override
    public byte[] write() {
        byte[] pageTypeId = new byte[0];

        if(m_pageTypeId!=null){
            pageTypeId = getUTF16Bytes(m_pageTypeId.toCharArray());
        }

        byte[] textureUrl = new byte[0];
        if(m_textureUrl != null) {
            textureUrl = getUTF16Bytes(m_textureUrl.toCharArray());
        }

        byte[] pointBytes=new byte[0];
        Iterator<Point> pointIterator = m_points.iterator();
        while(pointIterator.hasNext()){
            Point p = pointIterator.next();
            pointBytes = Utils.concatAll(pointBytes,
                    shortToByteArray(p.getX()),
                    shortToByteArray(p.getY()),
                    shortToByteArray(p.getInterval()));
        }
        byte[] remote = new byte[]{0x01};
        if(!m_remote) remote[0] = 0;
        byte[] uploadtexture = new byte[]{0x01};
        if(!m_uploadtexture) uploadtexture[0] = 0;

        byte[] result = Utils.concatAll(
                intToBytes(m_userId, ByteOrder.BIG_ENDIAN),//userid
                intToBytes(m_id, ByteOrder.BIG_ENDIAN),//stroke id
                intToBytes(pageTypeId.length, ByteOrder.BIG_ENDIAN),//pagetypeid
                pageTypeId,//pagetypeid
                intToBytes(m_pCount, ByteOrder.BIG_ENDIAN),//pointcount
                intToBytes(m_status.getValue(), ByteOrder.BIG_ENDIAN),//status
                intToBytes(m_pageId, ByteOrder.BIG_ENDIAN),//pageid
                intToBytes(m_pen.getColor(), ByteOrder.BIG_ENDIAN),//颜色
                intToBytes(m_pen.getRude(), ByteOrder.BIG_ENDIAN),//粗细
                intToBytes(m_pen.getType().ordinal(), ByteOrder.BIG_ENDIAN),//笔画类型
                shortToByteArray(m_areaWidth),//涂鸦区域
                shortToByteArray(m_areaHeight),//涂鸦区域
                shortToByteArray(m_width),//固定大小
                shortToByteArray(m_height),//固定大小
                intToBytes(m_points.size(), ByteOrder.BIG_ENDIAN),//点的集合
                pointBytes,//点的集合
                new byte[]{m_showFlag},//标记为
                intToBytes(textureUrl.length, ByteOrder.BIG_ENDIAN),//图片地址
                textureUrl,//图片地址
                remote,//是否远程
                uploadtexture,//是否上传
                new byte[]{textFlag});

        if(textFlag == 1){
            byte[] textByte = new byte[0];
            byte[] fontNameByte = new byte[0];
            try {
                textByte = text.getBytes("UTF-8");
                fontNameByte = fontName.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            result = Utils.concatAll(
                    result,
                    intToBytes(textByte.length, ByteOrder.BIG_ENDIAN),
                    textByte,
                    intToBytes(fontNameByte.length, ByteOrder.BIG_ENDIAN),
                    fontNameByte,
                    new byte[]{fontSize});
        }
        return result;
    }

    @Override
    public void read(ByteBufferList bb) {
        m_userId = bb.getInt();
        m_id = bb.getInt();
        m_pageTypeId = getUTFStrFormBytes(bb.getBytes(bb.getInt()));
        m_pCount = bb.getInt();
        m_status = BrushState.getByValue(bb.getInt());
        m_pageId = bb.getInt();
        m_pen.setColor(bb.getInt());
        m_pen.setRude(bb.getInt());
        int length = bb.getInt();
        if(length> StrokeType.values().length){//TODO:收到一个相当大的笔画类型导致越界
//            m_pen.setType(StrokeType.ST_LINE);
            Logger.e("StrokeType ArrayIndexOutOfBoundsException index ==>%s",length);
        }else {
            m_pen.setType(StrokeType.values()[length]);
        }
        m_areaWidth = bb.getShort();
        m_areaHeight = bb.getShort();
        m_width = bb.getShort();
        m_height = bb.getShort();
        int listCnt = bb.getInt();
        for (int i = 0; i < listCnt; i++) {
            Point p = new Point();
            p.x = bb.getShort();
            p.y = bb.getShort();
            p.interval = bb.getShort();
            if(!m_points.contains(p))
            m_points.add(p);
        }
        m_showFlag = bb.get();
        int imgLen = bb.getInt();
        if (imgLen > 0) {
            m_textureUrl = getUTFStrFormBytes(bb.getBytes(imgLen));

        }
        m_remote = bb.get() != 0;
        m_uploadtexture = bb.get() != 0;
        textFlag = bb.get();
        if (textFlag == 1) {
            int textlen = bb.getInt();
            if (textlen > 0) {
                text = getUTFStrFormBytes(bb.getBytes(textlen));
            }
            int fontNameLen = bb.getInt();
            if (fontNameLen > 0) {
                fontName = getUTFStrFormBytes(bb.getBytes(fontNameLen));

            }
            fontSize = bb.get();
        }


    }

    @Override
    public String toString() {
        return "BrushData{" +
                "m_userId=" + m_userId +
                ", m_pageTypeId='" + m_pageTypeId + '\'' +
                ", m_pageId=" + m_pageId +
                ", m_id=" + m_id +
                ", m_status=" + m_status +
                ", m_areaWidth=" + m_areaWidth +
                ", m_areaHeight=" + m_areaHeight +
                ", m_width=" + m_width +
                ", m_height=" + m_height +
                ", m_pen=" + m_pen +
                ", m_pCount=" + m_pCount +
                ", m_points=" + m_points+
                ", m_index=" + m_index +
                ", m_offset=" + m_offset +
                ", m_showFlag=" + m_showFlag +
                ", textFlag=" + textFlag +
                ", m_textureUrl='" + m_textureUrl + '\'' +
                ", m_remote=" + m_remote +
                ", m_uploadtexture=" + m_uploadtexture +
                ", text='" + text + '\'' +
                ", fontName='" + fontName + '\'' +
                ", fontSize=" + fontSize +
                '}';
    }
}
