package com.lpc.androidbasedemo.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.VideoView

/**
 * @author : lipengcheng1
 * @date : 2020-02-06
 * desc:
 */

class FullScreenVideoView  constructor(context: Context , attributeSet: AttributeSet? , defStyleAttr:Int): VideoView(context , attributeSet , defStyleAttr) {
    constructor(context: Context, attributeSet: AttributeSet?):this(context, attributeSet , 0){

    }

    constructor(context: Context):this(context , null){

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = View.getDefaultSize(0 , widthMeasureSpec)
        var height = View.getDefaultSize(0 , heightMeasureSpec);
        setMeasuredDimension(width , height)
    }
}