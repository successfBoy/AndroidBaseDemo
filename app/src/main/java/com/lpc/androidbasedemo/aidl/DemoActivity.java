package com.lpc.androidbasedemo.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.view.ImageItem;
import com.lpc.androidbasedemo.view.MultiTypeAdapter;
import com.sixfriend.shiyou.mylibrary.plugin.PluginManager;
import com.sixfriend.shiyou.mylibrary.plugin.PoxyActivity;
import com.sixfriend.shiyou.mylibrary.plugin.Utils;

import java.util.ArrayList;

/*
 * @author lipengcheng
 * create at  2019/5/19
 * description:
 */
public class DemoActivity extends Activity {
    private final static String TAG = DemoActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        DemoService service = new DemoService();
        IBinder iBinder = service.onBind(getIntent());
        IDemoAidlInterface iDemoAidlInterface = DemoStub.asInterface(iBinder);
        try {
            String name = iDemoAidlInterface.getName();
            iDemoAidlInterface.setName("mengyingying");
            String name2 = iDemoAidlInterface.getName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

//        Intent intent = new Intent(DemoActivity.this,)

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(manager);
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        for (int i = 0; i < 100; i++) {
            adapter.addItem(new ImageItem());
        }
        recyclerView.setAdapter(adapter);

//        ArrayList<String> arrayList = new ArrayList<>();
//        String a;
//        if(arrayList != null && (a = arrayList.get(0)) != null){
//            a.split("8");
//        }

        PluginManager.getInstance().init(this);

        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: action down");
                        break;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: ACTION_BUTTON_PRESS");
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: ACTION_HOVER_MOVE x: "+event.getRawX()+" y: "+event.getRawY());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: ACTION_UP");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: ACTION_MOVE x: "+event.getRawX()+" y: "+event.getRawY());
                        break;
                }
                return false;
            }
        });
        btn1.setText("loadApk");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apkPath = "sdcard/aa.apk";
                PluginManager.getInstance().loadApk(apkPath);
            }
        });
        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: action down");
                        break;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: ACTION_BUTTON_PRESS");
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: ACTION_HOVER_MOVE x: "+event.getRawX()+" y: "+event.getRawY());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: ACTION_UP");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, ((Button)v).getText().toString()+"----onTouch: ACTION_MOVE x: "+event.getRawX()+" y: "+event.getRawY());
                        break;
                }
                return false;
            }
        });
        btn2.setText("jump");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoActivity.this, PoxyActivity.class);
                intent.putExtra("className","com.sixfriend.shiyou.plugin.NewPluginActivity");
                startActivity(intent);
            }
        });
    }

    private Messenger mMessenger;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mMessenger = new Messenger(service);
            Message message = Message.obtain();

            message.replyTo = new Messenger(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
