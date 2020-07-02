package com.demo.recyclerxulc

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lpc.androidbasedemo.R
import com.lpc.androidbasedemo.activity.adapter.ItemTouchHelperAdapter
import com.lpc.androidbasedemo.activity.adapter.OnItemClickListener
import com.lpc.androidbasedemo.activity.bean.FunctionItem
import java.util.*

class FunctionBlockAdapter(private val context: Context, data: MutableList<FunctionItem>) : RecyclerView.Adapter<FunctionBlockAdapter.ViewHolder>(), ItemTouchHelperAdapter {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mIsEdit: Boolean = false
    private var data: MutableList<FunctionItem> = ArrayList<FunctionItem>()
    private val inflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.layout_grid_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fi: FunctionItem? = data[position]
        setImage(fi?.imageUrl, holder.iv)
        holder.text.setText(fi?.name)
        holder.btn.setImageResource(R.mipmap.ic_block_delete)

        if (mIsEdit) {
            holder.btn.visibility = View.VISIBLE
        } else {
            holder.btn.visibility = View.GONE
        }
        holder.btn.setOnClickListener {
            val fi: FunctionItem? = data.removeAt(position)
            if (listener != null) {
                listener!!.remove(fi)
            }
            notifyDataSetChanged()
        }

        holder.item.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (!mIsEdit && fi != null) {
                    mOnItemClickListener?.onItemClick(fi)
                }
            }
        })
    }

    fun setImage(url: String?, iv: ImageView) {
        try {
            val rid = context.resources.getIdentifier(url, "drawable", context.packageName)
            iv.setImageResource(rid)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onItemMove(holder: RecyclerView.ViewHolder?, fromPosition: Int, targetPosition: Int) {
        if (fromPosition < data.size && targetPosition < data.size) {
            Collections.swap(data, fromPosition, targetPosition)
            notifyItemMoved(fromPosition, targetPosition)
        }
    }

    override fun onItemSelect(holder: RecyclerView.ViewHolder?) {
        holder?.itemView?.scaleX = 0.8f
        holder?.itemView?.scaleY = 0.8f
    }

    override fun onItemClear(holder: RecyclerView.ViewHolder?) {
        holder?.itemView?.scaleX = 1.0f
        holder?.itemView?.scaleY = 1.0f
    }

    override fun onItemDismiss(holder: RecyclerView.ViewHolder?) {}
    override fun setIsEdit(edit: Boolean) {
        this.mIsEdit = edit;
        notifyDataSetChanged()
    }

    override fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv: ImageView
        val btn: ImageView
        val text: TextView
        val item: View

        init {
            item = itemView
            iv = itemView.findViewById<View>(R.id.iv) as ImageView
            text = itemView.findViewById<View>(R.id.text) as TextView
            btn = itemView.findViewById<View>(R.id.btn) as ImageView
        }
    }

    interface OnItemRemoveListener {
        fun remove(item: FunctionItem?)
    }

    private var listener: OnItemRemoveListener? = null
    fun setOnItemRemoveListener(listener: OnItemRemoveListener?) {
        this.listener = listener
    }

    init {
        inflater = LayoutInflater.from(context)
        if (data != null) {
            this.data = data
        }
    }


}