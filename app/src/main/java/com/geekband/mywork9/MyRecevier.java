package com.geekband.mywork9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Hyper on 2016/3/24.
 */
public class MyRecevier extends BroadcastReceiver {
    public static final String FLAG=MyRecevier.class.getSimpleName();
    private MusicPlayerApp application;

    @Override
    public void onReceive(Context context, Intent intent) {
        application =(MusicPlayerApp)context.getApplicationContext();
        Intent mIntent=new Intent(MainActivity.Filter);
        String value=intent.getAction();

        if (TextUtils.equals(value,"play")){
            Log.i("MyRecevier", "play");
            mIntent.putExtra(FLAG,PlayService.NOTIFICATION_PLAY);
        }else if (TextUtils.equals(value,"pause")){
            Log.i("MyRecevier", "pause");
            mIntent.putExtra(FLAG,PlayService.NOTIFICATION_PAUSE);

        }else if(TextUtils.equals(value,"next")){
            Log.i("MyRecevier", "next");
            mIntent.putExtra(FLAG,PlayService.NOTIFICATION_NEXT);
        }else if (TextUtils.equals(value,"previous")){
            Log.i("MyRecevier", "previous");
            mIntent.putExtra(FLAG,PlayService.NOTIFICATION_PREVIOUS);
        }
       application.locaBroadcastManager.sendBroadcast(mIntent);
    }

}
