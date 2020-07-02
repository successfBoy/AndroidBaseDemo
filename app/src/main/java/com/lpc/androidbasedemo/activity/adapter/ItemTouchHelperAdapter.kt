package com.lpc.androidbasedemo.activity.adapter

import android.support.v7.widget.RecyclerView

/**
 * @author : lipengcheng1
 * @date : 2020/7/2
 * desc:
 */


open interface ItemTouchHelperAdapter {
    fun onItemMove(holder: RecyclerView.ViewHolder?, fromPosition: Int, targetPosition: Int)
    fun onItemSelect(holder: RecyclerView.ViewHolder?)
    fun onItemClear(holder: RecyclerView.ViewHolder?)
    fun onItemDismiss(holder: RecyclerView.ViewHolder?)
    fun setIsEdit(edit: Boolean)
    fun setOnItemClickListener(listener: OnItemClickListener);
}