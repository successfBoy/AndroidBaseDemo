package com.lpc.androidbasedemo.thirdsdk.share.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;


import com.lpc.androidbasedemo.BuildConfig;

import java.io.File;

/*
 * @author lipengcheng
 * @emil lipengcheng1@jd.com
 * create at  2018/10/8
 * description:
 */
public class InstagramShareUtils {
    static String type = "image/*";
    static String filename = "/myPhoto.jpg";
    static String mediaPath = "sdcard" + filename;
    public static void share(Activity activity){
        createInstagramIntent(activity,type,mediaPath);
    }

    private static void createInstagramIntent(Activity activity,String type, String mediaPath){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        if (!media.exists()){
            Toast.makeText(activity,"不存在",Toast.LENGTH_LONG).show();
            return;
        }

        Uri uri = Uri.fromFile(media);

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileProvider", media);
//            share.setDataAndType(uri, "application/vnd.android.package-archive");
        }
//        else {
//            share.setDataAndType(uri, "application/vnd.android.package-archive");
//            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }







        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        activity.startActivity(Intent.createChooser(share, "Share to"));
    }
}
