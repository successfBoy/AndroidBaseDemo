package com.lpc.androidbasedemo.scribble.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wangzhiyuan on 2016/11/25.
 * desc:
 */
public class MyImage extends Action {
    private final Paint paint;
    private Point p;
    private float startX;
    private float startY;
    Bitmap bitmap;
    private float stopX;
    private float stopY;

    public MyImage(BrushData brushData) {
        super(brushData);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = new Rect((int) startX, (int) startY, (int) stopX, (int) stopY);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, rect, paint);
            updateUserNamePosition((int) stopX, (int) stopY);
        }
    }

    @Override
    public void move(float mx, float my) {
        load();
    }

    public void load() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                scribbleView.drawAction(false);//Todo:这样有点强耦合,先实现功能.
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (null != brushData) {
                        bitmap = getBitmapFromURL(brushData.getM_textureUrl());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void updateAction() {
        super.updateAction();
        p = brushData.getM_points().get(0);

        startX = getRationx() * (p.x - brushData.getM_width() / 2);
        startY = getRationy() * (p.y - brushData.getM_height() / 2);
        stopX = getRationx() * (p.x + brushData.getM_width() / 2);
        stopY = getRationy() * (p.y + brushData.getM_height() / 2);

        move(startX, startY);
    }

    public Bitmap getBitmapFromURL(String urlPath) {
        if (TextUtils.isEmpty(urlPath)) {
            return null;
        }
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

//            //设置图片压缩
//            //需要的大小
//            float newWidth = 200f;
//            float newHeigth = 200f;
//            //图片大小
//            int width = myBitmap.getWidth();
//            int height = myBitmap.getHeight();
//            //缩放比例
//            float scaleWidth = newWidth / width;
//            float scaleHeigth = newHeigth / height;
//            Matrix matrix = new Matrix();
//            matrix.postScale(scaleWidth, scaleHeigth);
//            Bitmap bitmap = Bitmap.createBitmap(myBitmap, 0, 0, width, height, matrix, true);

            return myBitmap;
        } catch (IOException e) {
            Log.e("MyImage", e.getMessage());
        }
        return null;
    }
}