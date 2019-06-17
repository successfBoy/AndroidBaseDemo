package com.lpc.androidbasedemo.view;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/*
 * @author lipengcheng
 * create at  2019-06-10
 * description:
 */
public class BindingUtil {
    @BindingAdapter(value = {"imageUrl","error","placeholder"},requireAll = false)
    public static void loadImage(ImageView imgView,
                                 String url,
                                 Drawable error,
                                 Drawable placeholder) {
        Glide.with(imgView.getContext())
                .load(url)
//                .error(error)
//                .placeholder(placeholder)
                .into(imgView);
    }
}
