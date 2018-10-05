package com.lpc.androidbasedemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.algorithmic.view.activity.AlgorithmicActivity;
import com.lpc.androidbasedemo.reactnative.ReactNativeActivity;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn =(Button) findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,ReactNativeActivity.class));
//            }
//        });
        btn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,AlgorithmicActivity.class));
        });

        /**
         * 存在于.class文件中的常量池，在运行期被JVM装载，并且可以扩充。String的intern()方法就是扩充常量池的一个方法；当一个String实例str调用intern()方法时，Java查找常量池中是否有相同Unicode的字符串常量，如果有，则返回其的引用，如果没有，则在常量池中增加一个Unicode等于str的字符串并返回它的引用
         */
        String str = new String("abc");
        str.intern();

        OkHttpClient httpClient= new OkHttpClient();
    }
}
