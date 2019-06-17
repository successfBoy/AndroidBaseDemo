package com.lpc.androidbasedemo.activity;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.aidl.DemoActivity;
import com.lpc.androidbasedemo.initenttest.FirstActivity;
import com.lpc.androidbasedemo.thirdsdk.share.ShareActivity;
import com.lpc.androidbasedemo.view.ThreeDSlidingLayout;
import com.lpc.googlesigin.processor.CustomAnnotation;

@CustomAnnotation("com.lpc.androidbasedemo.activity.MainActivity")
public class MainActivity extends BaseActivity {
    public static String TAG = "MainActivity";
    public static int testNum = 0;

    /**
     * 侧滑布局对象，用于通过手指滑动将左侧的菜单布局进行显示或隐藏。
     */
    private ThreeDSlidingLayout slidingLayout;

    /**
     * menu按钮，点击按钮展示左侧布局，再点击一次隐藏左侧布局。
     */
    private Button menuButton;

    /**
     * 放在content布局中的ListView。
     */
    private ListView contentListView;

    /**
     * 作用于contentListView的适配器。
     */
    private ArrayAdapter<String> contentListAdapter;

    /**
     * 用于填充contentListAdapter的数据源。
     */
    private String[] contentItems = {"Content Item 1", "Content Item 2", "Content Item 3",
            "Content Item 4", "Content Item 5", "Content Item 6", "Content Item 7",
            "Content Item 8", "Content Item 9", "Content Item 10", "Content Item 11",
            "Content Item 12", "Content Item 13", "Content Item 14", "Content Item 15",
            "Content Item 16"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        slidingLayout = (ThreeDSlidingLayout) findViewById(R.id.slidingLayout);
        menuButton = (Button) findViewById(R.id.menuButton);
        contentListView = (ListView) findViewById(R.id.contentList);
        contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                contentItems);
        contentListView.setAdapter(contentListAdapter);
        // 将监听滑动事件绑定在contentListView上
        slidingLayout.setScrollEvent(contentListView);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slidingLayout.isLeftLayoutVisible()) {
                    slidingLayout.scrollToRightLayout();
                } else {
                    slidingLayout.scrollToLeftLayout();
                }
            }
        });
        contentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = contentItems[position];
                testNum = 2;
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
//                startActivity(new Intent("com.test.firstactivity"));
                startActivity(new Intent(MainActivity.this, DemoActivity.class));
            }
        });

        com.lpc.googlesigin.processor.generated.GeneratedClass generatedClass = new com.lpc.googlesigin.processor.generated.GeneratedClass();
        String message = generatedClass.getMessage();
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
//        bindService(new Intent(this, BindTestService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
//        mService.getCount();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper mainLooper = MainActivity.this.getMainLooper();
//                Looper.prepare();

                final Handler handler = new Handler(getMainLooper()){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.i(TAG, "handleMessage: "+msg.what);
//                        msg.getTarget().getLooper().quit();
                    }
                };

                handler.sendEmptyMessage(0);
                Log.i(TAG, "run: "+"000000");
                Log.i(TAG, "run: "+"11111");
//                Looper.loop();
                Log.i(TAG, "run: "+"22222");
                Log.i(TAG, "run: "+"33333");

            }
        }).start();


    }

    BindTestService mService;
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BindTestService.LocatBinder binder = (BindTestService.LocatBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}
