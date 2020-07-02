package com.lpc.androidbasedemo.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lpc.androidbasedemo.R
import com.lpc.androidbasedemo.activity.bean.FunctionItem
import java.util.*

/**
 * @author : lipengcheng1
 * @date : 2020/7/2
 * desc:
 */
class FunctionAdapter(context: Context, data: MutableList<FunctionItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mIsEdit: Boolean = false
    private var data: MutableList<FunctionItem> = ArrayList<FunctionItem>()
    private val inflater: LayoutInflater
    private val context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder: RecyclerView.ViewHolder
        holder = if (0 == viewType) {
            TitleViewHolder(inflater.inflate(R.layout.layout_function_text, parent, false))
        } else {
            FunctionViewHolder(inflater.inflate(R.layout.layout_grid_item, parent, false))
        }
        return holder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (0 == getItemViewType(position)) {
            val holder = viewHolder as TitleViewHolder
            holder.text.setText(data[position].name)
        } else {
            val holder = viewHolder as FunctionViewHolder
            val fi: FunctionItem = data[position]
            setImage(fi.imageUrl, holder.iv)
            holder.text.setText(fi.name)
            holder.btn.setImageResource(if (fi.isSelect) R.mipmap.ic_block_selected else R.mipmap.ic_block_add)
            if (mIsEdit) {
                holder.btn.visibility = View.VISIBLE
            } else {
                holder.btn.visibility = View.GONE
            }
            holder.btn.setOnClickListener {
                val f: FunctionItem = data[position]
                if (!f.isSelect) {
                    if (listener != null) {
                        if (listener!!.add(f)) {
                            f.isSelect = true
                            notifyDataSetChanged()
                        }
                    }
                }
            }

            holder.item.setOnClickListener(object :View.OnClickListener{
                override fun onClick(v: View?) {
                    if (!mIsEdit)
                    mOnItemClickListener?.onItemClick(fi)
                }
            })
        }
    }

    fun setImage(url: String?, iv: ImageView) {
        try {
            val rid = context.resources.getIdentifier(url, "drawable", context.packageName)
            iv.setImageResource(rid)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].isTitle) 0 else 1
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView

        init {
            text = itemView.findViewById<View>(R.id.text) as TextView
        }
    }

    private inner class FunctionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val iv: ImageView
         val btn: ImageView
         val text: TextView
        val item:View

        init {
            item = itemView
            iv = itemView.findViewById<View>(R.id.iv) as ImageView
            text = itemView.findViewById<View>(R.id.text) as TextView
            btn = itemView.findViewById<View>(R.id.btn) as ImageView
        }
    }

    interface OnItemAddListener {
        fun add(item: FunctionItem?): Boolean
    }

    private var listener: OnItemAddListener? = null
    fun setOnItemAddListener(listener: OnItemAddListener?) {
        this.listener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.mOnItemClickListener = listener
    }

    fun setIsEdit(edit: Boolean) {
        this.mIsEdit = edit
        notifyDataSetChanged()
    }

    init {
        this.context = context
        if (data != null) {
            this.data = data
        }
        inflater = LayoutInflater.from(context)
    }
}