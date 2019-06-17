package com.lpc.androidbasedemo.thirdsdk.share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.activity.BaseActivity;
import com.lpc.androidbasedemo.thirdsdk.share.utils.InstagramShareUtils;
import com.lpc.androidbasedemo.thirdsdk.share.utils.MessengerShareUtils;

/*
 * @author lipengcheng
 * @emil lipengcheng1@jd.com
 * create at  2018/10/8
 * description:
 */
public class ShareActivity extends BaseActivity implements View.OnClickListener {
    private static LeakCanaryDemo sLeakCanaryDemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == sLeakCanaryDemo) {
            sLeakCanaryDemo = new LeakCanaryDemo();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    public void initView() {
        Button messengerShareButton = (Button) findViewById(R.id.fb_share_button);
        messengerShareButton.setOnClickListener(this);
        Button instagramShareButton = (Button) findViewById(R.id.instagram_share_button);
        instagramShareButton.setOnClickListener(this);

    }

    class LeakCanaryDemo {

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_share_button:
                MessengerShareUtils.fbMessengerShare(ShareActivity.this);
                break;
            case R.id.instagram_share_button:
                InstagramShareUtils.share(ShareActivity.this);
                break;
        }
    }
}
