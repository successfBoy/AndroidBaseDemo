package com.lpc.androidbasedemo.algorithmic.presenter;

import com.lpc.androidbasedemo.algorithmic.model.AlgorithmicinfoCompl;
import com.lpc.androidbasedemo.algorithmic.view.activity.IAlgorithmicView;


/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/27
 * description:
 */
public class AlgorithmicPresenterCompl implements IAlgorithmicPresenter{
    private IAlgorithmicView algorithmicView;
    public AlgorithmicPresenterCompl(IAlgorithmicView algorithmicActivity) {
        this.algorithmicView=algorithmicActivity;
    }

    @Override
    public void showIAlgorithmicList() {

        algorithmicView.setAdapter(new AlgorithmicinfoCompl().getAllAlgorithmicInfos());
    }


}
