package com.lpc.androidbasedemo.algorithmic.view.activity;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.activity.BaseActivity;
import com.lpc.androidbasedemo.algorithmic.model.AlgorithmicinfoCompl;
import com.lpc.androidbasedemo.algorithmic.model.IAlgorithmicInfo;
import com.lpc.androidbasedemo.algorithmic.presenter.AlgorithmicPresenterCompl;
import com.lpc.androidbasedemo.algorithmic.presenter.IAlgorithmicPresenter;
import com.lpc.androidbasedemo.algorithmic.utils.CommonAlgorithmic;
import com.lpc.androidbasedemo.algorithmic.utils.Costants;
import com.lpc.androidbasedemo.algorithmic.utils.SortUtils;
import com.lpc.androidbasedemo.algorithmic.view.adapter.AlgorithmicAdpt;
import com.lpc.androidbasedemo.common.tool.LogUtils;
import com.lpc.androidbasedemo.common.tool.Utils;

import java.util.List;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/25
 * description: 算法
 */
public class AlgorithmicActivity extends BaseActivity implements IAlgorithmicView, View.OnClickListener {
    private String TAG = "AlgorithmicActivity";
    private RecyclerView mRecyclerView;
    private RelativeLayout mLayoutResult;
    private Button mCloseResult;

    private AlgorithmicAdpt mAdapt;
    private IAlgorithmicPresenter iAlgorithmicPresenter;

    private int a[] = {33, 21, 7, 43, 2, 89, 76, 100};


    @Override
    protected void initData() {
        iAlgorithmicPresenter = new AlgorithmicPresenterCompl(this);
        iAlgorithmicPresenter.showIAlgorithmicList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_algorithmic;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(layoutManage);
        mLayoutResult = (RelativeLayout) findViewById(R.id.ll_result);
        mCloseResult = (Button) findViewById(R.id.close);
        mCloseResult.setOnClickListener(this);
        Handler handler = new Handler();
    }

    @Override
    public void setAdapter(List<IAlgorithmicInfo> list) {
        mAdapt = new AlgorithmicAdpt(getContext(), list);
        mRecyclerView.setAdapter(mAdapt);
    }

    @Override
    public void onItemClickListener(AlgorithmicinfoCompl info) {

        switch (info.id) {
            case Costants.ALGORITHMIC_ID_FASTSORT:
                SortUtils.fastSort(a);
                break;
            case Costants.ALGORITHMIC_ID_MAOPAOSORT:
                SortUtils.maoPaoSort(a);
                break;
            case Costants.ALGORITHMIC_ID_SELECTSORTT:
                SortUtils.selectSort(a);
                break;
            case Costants.ALGORITHMIC_ID_REVERSENODE:
                CommonAlgorithmic.reverseListNode();
                break;
        }
//        dropResultLayout(false);
    }

    @Override
    public void dropResultLayout(boolean close) {
        int start = 0;
        int end = Utils.getHeight(getContext());
        ValueAnimator animator;
        LogUtils.i(close);
        if (close) {
            animator = ValueAnimator.ofInt(start, end);
        } else {
            animator = ValueAnimator.ofInt(end, start);

        }

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.topMargin = value;
                mLayoutResult.setLayoutParams(layoutParams);
            }
        });
        animator.setDuration(500);
        animator.start();
        mLayoutResult.setVisibility(View.VISIBLE);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dropResultLayout(true);
                break;
        }
    }
}
