package com.lpc.androidbasedemo.thirdsdk.share.utils;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.share.model.ShareMessengerGenericTemplateContent;
import com.facebook.share.model.ShareMessengerGenericTemplateElement;
import com.facebook.share.model.ShareMessengerURLActionButton;
import com.facebook.share.widget.MessageDialog;

/*
 * @author lipengcheng
 * @emil lipengcheng1@jd.com
 * create at  2018/10/8
 * description:
 */
public class MessengerShareUtils {
    public static void fbMessengerShare(Activity activity){
        ShareMessengerURLActionButton actionButton =
                new ShareMessengerURLActionButton.Builder()
                        .setTitle("Visit Facebook")
                        .setUrl(Uri.parse("https://www.facebook.com"))
                        .build();
        ShareMessengerGenericTemplateElement genericTemplateElement =
                new ShareMessengerGenericTemplateElement.Builder()
                        .setTitle("Visit Facebook")
                        .setSubtitle("Visit Messenger")
                        .setImageUrl(Uri.parse("heeps://Your/Image/Url"))
                        .setButton(actionButton)
                        .build();
        ShareMessengerGenericTemplateContent genericTemplateContent = new ShareMessengerGenericTemplateContent.Builder()
                .setPageId("Your Page Id") // Your page ID, required
                .setGenericTemplateElement(genericTemplateElement)
                .build();

        if (MessageDialog.canShow(ShareMessengerGenericTemplateContent.class)) {
            MessageDialog.show(activity, genericTemplateContent);
        }else{
            Toast.makeText(activity,"not install Messenger, please install the Messenger",Toast.LENGTH_LONG).show();
        }
    }
}
