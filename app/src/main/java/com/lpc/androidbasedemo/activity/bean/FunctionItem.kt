package com.lpc.androidbasedemo.activity.bean

/**
 * @author : lipengcheng1
 * @date : 2020/7/2
 * desc:
 */
class FunctionItem {
    var name: String
    var isSelect = false
    var imageUrl = ""
    var background = ""
    var isTitle = false
    var subItemCount = 0
    var jumpPath = ""

    constructor(name: String, isSelect: Boolean, imageUrl: String, background: String) {
        this.name = name
        this.isSelect = isSelect
        this.imageUrl = imageUrl
        this.background = background
    }

    constructor(name: String, isTitle: Boolean, subItemCount: Int) {
        this.name = name
        this.isTitle = isTitle
        this.subItemCount = subItemCount
    }

    constructor(name: String, isTitle: Boolean) {
        this.name = name
        this.isTitle = isTitle
    }
}