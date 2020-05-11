package com.lpc.androidbasedemo.scribble.entity;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhiyuan on 2016/12/11.
 * desc:
 */

public class MyText extends Action {
    private final Paint paint;
    private static final int newProtocalMagicNum = 29999;//新文字方案标识
    private float startX;
    private float startY;
    private List<Point> textPositionList = new ArrayList<>();
    private int textWidthRemote = -1;
    private int textHeightRemote = -1;

    public MyText(BrushData brushData) {
        super(brushData);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
//        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!TextUtils.isEmpty(brushData.text)) {
//            canvas.drawText(brushData.text, startX, startY, paint);
            drawTextOnCanvas(canvas, brushData.text);
            updateUserNamePosition((int) startX,(int) startY);
        }
    }

    @Override
    public void move(float mx, float my) {

    }

    @Override
    public void updateAction() {
        super.updateAction();
        Resources r = context.getResources(); // 取得手機資源
        float fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, //轉換dp值
                brushData.fontSize, //dp值
                r.getDisplayMetrics());

//        int fontSize = DisplayUtil.sp2px(px);
//        paint.setTextSize(fontSize);
        int brushPointSize = brushData.getM_points().size();
        int textSize = brushData.text.length();
        if (brushPointSize>0&&
                brushPointSize == textSize+2
                &&brushData.getM_points().get(brushPointSize-1).getX()==newProtocalMagicNum
                &&brushData.getM_points().get(brushPointSize-1).getY()==newProtocalMagicNum){//新文字方案协议
            textPositionList.clear();
            for (int i=0;i<brushPointSize-1;i++){
                if (i==0){
                    Point p = brushData.getM_points().get(i);
                    startX = p.x * getRationx();
                    startY = p.y * getRationy();
                    textPositionList.add(brushData.getM_points().get(i));
                }else if (i==brushPointSize-2){
                    textWidthRemote = brushData.getM_points().get(i).getX();
                    textHeightRemote = brushData.getM_points().get(i).getY();
                    if (scribbleView.getHeight() !=0&&brushData.getM_areaHeight() !=0){
                        int areaHeightRemote = brushData.getM_areaWidth()*3/4;
                        int areaHeightLocal = scribbleView.getWidth() *3/4;
                        textHeightRemote = textHeightRemote * areaHeightLocal/areaHeightRemote;
                    }
                }else {
                    Point point = brushData.getM_points().get(i);
                    Point pointTemp = new Point();
                    pointTemp.x = (short)(brushData.getM_points().get(0).x+point.x);
                    pointTemp.y = (short)(brushData.getM_points().get(0).y+point.y);
                    textPositionList.add(pointTemp);
                }
            }
            return;
        }

        for (int i = 0; i < brushData.getM_points().size(); i++) {
            Point p = brushData.getM_points().get(i);
            startX = p.x * getRationx();
            startY = p.y * getRationy();
        }
    }

    private void drawTextOnCanvas(Canvas canvas, String text) {
        // maybe color the bacground..
//        canvas.drawPaint(paint);


        // Setup a textview like you normally would with your activity context
//        tv.setTextSize(brushData.fontSize/2);
        int brushPointSize = brushData.getM_points().size();
        int textLength = text.length();
        if (brushPointSize>0&&
                brushPointSize == textLength+2
                &&brushData.getM_points().get(brushPointSize-1).getX()==newProtocalMagicNum
                &&brushData.getM_points().get(brushPointSize-1).getY()==newProtocalMagicNum) {//新文字方案协议 start
            if (textPositionList.size() != textLength){
                Log.e("drawTextOnCanvas","error");
                return;
            }
            TextView tempTextView = new TextView(scribbleView.getContext());
            tempTextView.setDrawingCacheEnabled(true);
            for (int i =0;i<text.length();i++){
                String charText = String.valueOf(text.charAt(i));
                tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,brushData.fontSize);
                tempTextView.setText(charText);
                tempTextView.setTextColor(color);
                tempTextView.measure(View.MeasureSpec.makeMeasureSpec((int) (brushData.getM_width()*getRationx()), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec((int) (brushData.getM_height()*getRationy()), View.MeasureSpec.EXACTLY));
                tempTextView.layout(0, 0,  (tempTextView.getMeasuredWidth()), (tempTextView.getMeasuredHeight()));
                float textX = textPositionList.get(i).getX()*getRationx();
                float textY = textPositionList.get(i).getY()*getRationy();
                int textHeightPaint = (int) Math.abs(tempTextView.getPaint().getFontMetrics().bottom - tempTextView.getPaint().getFontMetrics().top);
                int textFontSize = brushData.fontSize;
                while (textHeightPaint<textHeightRemote){//调整字体大小
                    textFontSize++;
                    tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textFontSize);
                    textHeightPaint = (int) Math.abs(tempTextView.getPaint().getFontMetrics().bottom - tempTextView.getPaint().getFontMetrics().top);
                }
                while (textHeightPaint>=textHeightRemote+1){
                    textFontSize--;
                    tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textFontSize);
                    textHeightPaint = (int) Math.abs(tempTextView.getPaint().getFontMetrics().bottom - tempTextView.getPaint().getFontMetrics().top);
                }
                if (textFontSize<=0){
                    Log.e("drawTextOnCanvas","error textFontSize:"+textFontSize);
                    return;
                }
                canvas.drawBitmap(tempTextView.getDrawingCache(), textX, textY, null);
            }
            tempTextView.setDrawingCacheEnabled(false);
            return;
        }//新文字方案协议 end

        TextView tv = new TextView(scribbleView.getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,brushData.fontSize);
        // setup text
        tv.setText(text);

        // maybe set textcolor
        tv.setTextColor(color);

        // you have to enable setDrawingCacheEnabled, or the getDrawingCache will return null
        tv.setDrawingCacheEnabled(true);

        // we need to setup how big the view should be..which is exactly as big as the canvas
        tv.measure(View.MeasureSpec.makeMeasureSpec((int) (brushData.getM_width()*getRationx()), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec((int) (brushData.getM_height()*getRationy()), View.MeasureSpec.EXACTLY));

        // assign the layout values to the textview
        tv.layout(0, 0,  (tv.getMeasuredWidth()), (tv.getMeasuredHeight()));

        // draw the bitmap from the drawingcache to the canvas
        canvas.drawBitmap(tv.getDrawingCache(), startX, startY, null);

        // disable drawing cache
        tv.setDrawingCacheEnabled(false);
    }


}
