package com.lpc.androidbasedemo.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/*
 * @author lipengcheng
 * create at  2019-06-10
 * description:
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private static final int ITEM_TYPE_HEADER = 0;
    private List<IItem> items = new ArrayList<>();
    private boolean showHeader;
    private boolean showFooter;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                viewType, parent, false);
        return new ItemViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindTo(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getLayoutType();
    }

    public void setItem(IItem item) {
        clearItems();
        addItem(item);
    }

    public void setItems(List<IItem> items) {
        clearItems();
        addItems(items);
    }

    public void addItem(IItem item) {
        items.add(item);
    }

    public void addItem(IItem item, int index) {
        items.add(index, item);
    }

    public void addItems(List<IItem> items) {
        this.items.addAll(items);
    }

    public void removeItem(IItem item) {
        items.remove(item);
    }

    public void clearItems() {
        items.clear();
    }

    public List<IItem> getItems() {
        return items;
    }

    public interface IItem {
        int getLayoutType();
    }
}
