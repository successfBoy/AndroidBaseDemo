package com.lpc.androidbasedemo.algorithmic.view.activity;

import com.lpc.androidbasedemo.algorithmic.model.AlgorithmicinfoCompl;
import com.lpc.androidbasedemo.algorithmic.model.IAlgorithmicInfo;

import java.util.List;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/27
 * description:
 */
public interface IAlgorithmicView {
    void setAdapter(List<IAlgorithmicInfo> list);
    void onItemClickListener(AlgorithmicinfoCompl info);
    void dropResultLayout(boolean close);
}
