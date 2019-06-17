package com.lpc.androidbasedemo.view;

import com.lpc.androidbasedemo.R;

public class HeaderItem implements MultiTypeAdapter.IItem {

    @Override
    public int getLayoutType() {
        return R.layout.item_header;
    }
}