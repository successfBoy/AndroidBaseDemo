package com.lpc.androidbasedemo.view;

import com.lpc.androidbasedemo.R;

import java.util.Random;

/*
 * @author lipengcheng
 * create at  2019-06-10
 * description:
 */
public class ImageItem implements MultiTypeAdapter.IItem {
    public final String url;

    public ImageItem() {
        url = "https://unsplash.it/200/200?random&" + new Random().nextInt(40);
    }
    @Override
    public int getLayoutType() {
        return R.layout.layout_item_image;
    }
}
