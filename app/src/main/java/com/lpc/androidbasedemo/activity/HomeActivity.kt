package com.lpc.androidbasedemo.activity

import android.app.Activity
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import com.alibaba.android.arouter.launcher.ARouter
import com.demo.recyclerxulc.FunctionBlockAdapter
import com.lpc.androidbasedemo.R
import com.lpc.androidbasedemo.activity.adapter.FunctionAdapter
import com.lpc.androidbasedemo.activity.adapter.OnItemClickListener
import com.lpc.androidbasedemo.activity.bean.FunctionItem
import com.lpc.androidbasedemo.activity.utils.SFUtils
import com.lpc.androidbasedemo.activity.view.SpaceItemDecoration
import com.lpc.androidbasedemo.scribble.utils.DisplayUtil.dip2px
import com.lpc.androidbasedemo.scribble.utils.ToastUtils
import java.util.*

/**
 * @author : lipengcheng1
 * @date : 2020/7/2
 * desc:
 */

class HomeActivity : BaseActivity() {

    private var recyclerViewExist: RecyclerView? = null
    private  var recyclerViewAll:RecyclerView? = null

    private var horizonLScrollView: HorizontalScrollView? = null
    private var rg_tab: RadioGroup? = null

    private var blockAdapter: FunctionBlockAdapter? = null
    private var functionAdapter: FunctionAdapter? = null
    private var gridManager: GridLayoutManager? = null

    private val scrollTab: MutableList<String> = ArrayList()

    private var itemWidth = 0
    private var lastRow = 0
    private var isMove = false //滑动状态

    private var scrollPosition = 0
    private var currentTab //当前的标签
            : String? = null
    private var tabWidth = 0 //标签宽度

    private var isEdit = false


    private  lateinit var  allData: MutableList<FunctionItem>
    private lateinit var selData: MutableList<FunctionItem>
    private var sfUtils: SFUtils? = null
    private val MAX_COUNT = 14
    private var isDrag = false

    private val onItemClickListener: OnItemClickListener =object :OnItemClickListener{
        override fun onItemClick(item: FunctionItem) {
            if (TextUtils.isEmpty(item.jumpPath)){

                ToastUtils.show(this@HomeActivity , "点击" + item.name)
            } else {
                ARouter.getInstance().build(item.jumpPath).navigation()
            }
        }

    }
    override fun initView() {
//        supportActionBar!!.hide()
        recyclerViewExist = findViewById<View>(R.id.recyclerViewExist) as RecyclerView
        horizonLScrollView = findViewById<View>(R.id.horizonLScrollView) as HorizontalScrollView
        rg_tab = findViewById<View>(R.id.rg_tab) as RadioGroup
        recyclerViewAll = findViewById<View>(R.id.recyclerViewAll) as RecyclerView
        sfUtils = SFUtils(this)
        allData = sfUtils!!.getAllFunctionWithState()
        selData = sfUtils!!.getSelectFunctionItem()


        blockAdapter = FunctionBlockAdapter(this, selData)
        recyclerViewExist?.setLayoutManager(GridLayoutManager(this, 4))
        recyclerViewExist?.setAdapter(blockAdapter)
        recyclerViewExist?.addItemDecoration(SpaceItemDecoration(4, dip2px(this, 10f)))

        val callback = DefaultItemCallback(blockAdapter)
        val helper = DefaultItemTouchHelper(callback)
        helper.attachToRecyclerView(recyclerViewExist)

        gridManager = GridLayoutManager(this, 4)
        gridManager?.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val fi: FunctionItem = allData!!.get(position)
                return if (fi.isTitle) 4 else 1
            }
        })

        functionAdapter = FunctionAdapter(this, allData!!)
        recyclerViewAll?.setLayoutManager(gridManager)
        recyclerViewAll?.setAdapter(functionAdapter)
        val spaceDecoration = SpaceItemDecoration(4, dip2px(this, 10f))
        recyclerViewAll?.addItemDecoration(spaceDecoration)

        itemWidth = getAtyWidth(this) / 4 + dip2px(this, 2f)

        resetEditHeight(selData!!.size)

        initTab()
    }

    override fun initData() {
        addListener()
    }

    fun getAtyWidth(context: Context): Int {
        return try {
            val mDm = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay
                    .getMetrics(mDm)
            mDm.widthPixels
        } catch (e: java.lang.Exception) {
            0
        }
    }

    fun addListener() {
        var editor:TextView = findViewById(R.id.submit)
        editor.setOnClickListener {
            isEdit = !isEdit
            blockAdapter?.setIsEdit(isEdit)
            functionAdapter?.setIsEdit(isEdit)
            if (isEdit) {
                editor.setText("保存")
            } else {
                sfUtils!!.saveSelectFunctionItem(selData)
                sfUtils!!.saveAllFunctionWithState(allData)
                editor.setText("编辑")
            }


        }
        functionAdapter!!.setOnItemAddListener(object : FunctionAdapter.OnItemAddListener {
            override fun add(item: FunctionItem?): Boolean {
                    return if (selData != null && selData!!.size < MAX_COUNT) {   // 更新选择列表，所有列表已在内部进行更新
                        try {
                            selData?.add(item!!)
                            resetEditHeight(selData!!.size)
                            blockAdapter!!.notifyDataSetChanged()
                            item?.isSelect = true
                            return true
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                        false
                    } else {
                        Toast.makeText(this@HomeActivity, "选中的模块不能超过" + MAX_COUNT + "个", Toast.LENGTH_SHORT).show()
                        false
                    }
            }
        })
        blockAdapter!!.setOnItemRemoveListener(object : FunctionBlockAdapter.OnItemRemoveListener {
            override fun remove(item: FunctionItem?) {
                // 更新所有列表，选择列表已在内部进行更新
                try {
                    if (item != null && item.name != null) {
                        for (i in allData!!.indices) {
                            val data: FunctionItem = allData!![i]
                            if (data != null && data.name != null) {
                                if (item.name == data.name) {
                                    data.isSelect = false
                                    break
                                }
                            }
                        }
                        functionAdapter!!.notifyDataSetChanged()
                    }
                    resetEditHeight(selData!!.size)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        })

        blockAdapter!!.setOnItemClickListener(onItemClickListener)
        functionAdapter!!.setOnItemClickListener(onItemClickListener)
        recyclerViewAll!!.addOnScrollListener(onScrollListener)
    }



    fun onClick(v: View?) {}

    private fun initTab() {
        try {
            val tabs: List<FunctionItem>? = sfUtils!!.tabNames
            if (tabs != null && tabs.size > 0) {
                currentTab = tabs[0].name
                val padding = dip2px(this, 10f)
                val size = tabs.size
                for (i in 0 until size) {
                    val item: FunctionItem = tabs[i]
                    if (item.isTitle) {
                        scrollTab.add(item.name)
                        val rb = RadioButton(this)
                        rb.setPadding(padding, 0, padding, 0)
                        rb.buttonDrawable = null
                        rb.gravity = Gravity.CENTER
                        rb.setText(item.name)
                        rb.tag = item.subItemCount
                        rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                        try {
                            rb.setTextColor(resources.getColorStateList(R.color.bg_block_text))
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                        rb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resources.getDrawable(R.drawable.bg_block_tab))
                        rb.setOnCheckedChangeListener(onCheckedChangeListener)
                        rg_tab!!.addView(rb)
                    }
                }
                (rg_tab!!.getChildAt(0) as RadioButton).isChecked = true
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        try {
            val position = buttonView.tag as Int
            val text = buttonView.text.toString()
            if (currentTab != text && isChecked) {
                currentTab = text
                moveToPosition(position)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun resetEditHeight(size: Int) {
        var size = size
        try {
            if (size == 0) {
                size = 1
            }
            var row = size / 4 + if (size % 4 > 0) 1 else 0
            if (row <= 0) row = 1
            if (lastRow != row) {
                lastRow = row
                val params = recyclerViewExist!!.layoutParams
                params.height = itemWidth * row
                recyclerViewExist!!.layoutParams = params
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun moveToPosition(position: Int) {
        val first = gridManager!!.findFirstVisibleItemPosition()
        val end = gridManager!!.findLastVisibleItemPosition()
        if (first == -1 || end == -1) return
        if (position <= first) {      //移动到前面
            gridManager!!.scrollToPosition(position)
        } else if (position >= end) {      //移动到后面
            isMove = true
            scrollPosition = position
            gridManager!!.smoothScrollToPosition(recyclerViewAll, null, position)
        } else { //中间部分
            val n = position - gridManager!!.findFirstVisibleItemPosition()
            if (n > 0 && n < allData!!.size) {
                val top = gridManager!!.findViewByPosition(position).top
                recyclerViewAll!!.scrollBy(0, top)
            }
        }
    }

    private val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            try {
                if (isMove && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isMove = false
                    val view = gridManager!!.findViewByPosition(scrollPosition)
                    if (view != null) {
                        recyclerView.scrollBy(0, view.top)
                    }
                }
                isDrag = if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    true
                } else {
                    false
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (isDrag) {  //拖动过程中
                val position = gridManager!!.findFirstVisibleItemPosition()
                if (position > 0) {
                    for (i in 0 until position + 1) {
                        if (allData!![i].isTitle) {
                            currentTab = allData!![i].name
                        }
                    }
                    scrollTab(currentTab!!)
                }
            }
        }
    }

    private fun scrollTab(newTab: String) {
        try {
            val position = scrollTab.indexOf(currentTab)
            val targetPosition = scrollTab.indexOf(newTab)
            currentTab = newTab
            if (targetPosition != -1) {
                val x = (targetPosition - position) * getTabWidth()
                val radioButton = rg_tab!!.getChildAt(targetPosition) as RadioButton
                radioButton.setOnCheckedChangeListener(null)
                radioButton.isChecked = true
                radioButton.setOnCheckedChangeListener(onCheckedChangeListener)
                horizonLScrollView!!.scrollBy(x, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getTabWidth(): Int {
        if (tabWidth == 0) {
            if (rg_tab != null && rg_tab!!.childCount != 0) {
                tabWidth = rg_tab!!.width / rg_tab!!.childCount
            }
        }
        return tabWidth
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

}