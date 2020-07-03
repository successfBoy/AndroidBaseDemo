package com.lpc.androidbasedemo.view;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/*
 * @author lipengcheng
 * create at  2019-06-10
 * description:
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {
    public ViewDataBinding mBinding;
    public ItemViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bindTo(MultiTypeAdapter.IItem iItem){
//        mBinding.setVariable(BR.item, iItem);
        mBinding.executePendingBindings();
    };
}
