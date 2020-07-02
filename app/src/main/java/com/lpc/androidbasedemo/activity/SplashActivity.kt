package com.lpc.androidbasedemo.activity

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import com.lpc.androidbasedemo.R
import com.lpc.androidbasedemo.initenttest.FirstActivity
import com.lpc.androidbasedemo.view.FullScreenVideoView
import java.io.File

/**
 * @author : lipengcheng1
 * @date : 2020-02-04
 * desc:
 */

class SplashActivity : Activity() {
    private var mVideoView: FullScreenVideoView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
        Handler().postDelayed(Runnable {
            goMain()
        } , 3000)
    }


    fun goMain(){
        var intent = Intent(this@SplashActivity , HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initView() {
        mVideoView = findViewById(R.id.vv_play)
        mVideoView?.setVideoURI(Uri.parse("android.resource://"+packageName+ File.separator+R.raw.splash));
        mVideoView?.setOnPreparedListener(object : MediaPlayer.OnPreparedListener{
            override fun onPrepared(mp: MediaPlayer?) {
                mp?.start()
            }

        })

        mVideoView?.setOnCompletionListener { object : MediaPlayer.OnCompletionListener{
            override fun onCompletion(mp: MediaPlayer?) {
                mp?.start()
            }

        } }
    }
}