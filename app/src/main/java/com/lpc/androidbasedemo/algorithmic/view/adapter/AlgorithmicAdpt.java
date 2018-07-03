package com.lpc.androidbasedemo.algorithmic.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.algorithmic.model.AlgorithmicinfoCompl;
import com.lpc.androidbasedemo.algorithmic.model.IAlgorithmicInfo;
import com.lpc.androidbasedemo.algorithmic.view.activity.IAlgorithmicView;
import com.lpc.androidbasedemo.common.tool.LogUtils;

import java.util.List;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/27
 * description:
 */
public class AlgorithmicAdpt extends Adapter{
    Context mContext;
    List<IAlgorithmicInfo> list;
    LayoutInflater inflater;

    public AlgorithmicAdpt(Context context, List<IAlgorithmicInfo> list) {
        this.list=list;
        this.mContext=context;
        inflater=LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_algorithmic_item,parent,false);
        return new AlgorithmicViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((AlgorithmicViewHolder)holder).name.setText(list.get(position).getName());


    }

    public AlgorithmicinfoCompl getItem(int position){
        return (AlgorithmicinfoCompl) list.get(position);
    };
    @Override
    public int getItemCount() {
        return list.size();
    }
    class AlgorithmicViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public AlgorithmicViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.i("onBind");
                    ((IAlgorithmicView)mContext).onItemClickListener(getItem(getLayoutPosition()));
                }
            });

        }

    }
}
