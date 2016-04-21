package com.geekband.mywork9;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import java.util.ArrayList;

public class PlayService extends Service {

    private RemoteViews remoteViews;
    private Notification.Builder builder= null;
    private NotificationManager notificationManager;

    private int mInfo=0;
    private MediaPlayer mMediaPlayer;
    private ArrayList<Integer> mArrayList=new ArrayList<>();
    private ArrayList<String> titleArrayList =new ArrayList<>();
    private ArrayList<String> singerArrayList=new ArrayList<>();

    private Intent mIntent;

    public int CUSTOM_VIEW_ID=1;
    public Boolean STATUS=false;

    public static final String NOTIFICATION_PLAY="play";
    public static final String NOTIFICATION_NEXT="next";
    public static final String NOTIFICATION_PREVIOUS="previous";
    public static final String NOTIFICATION_PAUSE="pause";

    Messenger mMessenger=new Messenger(new IncomingHandler());

    class IncomingHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MainActivity.PLAY_CODE:
                    mMediaPlayer.start();
                    STATUS=true;
                    setNotification(STATUS);
                    Toast.makeText(getApplicationContext(),"PLAY",Toast.LENGTH_SHORT).show();
                    break;

                case MainActivity.PAUSE_CODE:
                    mMediaPlayer.pause();
                    STATUS=false;
                    setNotification(STATUS);
                    Toast.makeText(getApplicationContext(),"PAUSE",Toast.LENGTH_SHORT).show();
                    break;
                case MainActivity.NEXT_CODE:
                    if (mMediaPlayer.isPlaying()){
                        mMediaPlayer.stop();
                    }
                    if (mInfo==mArrayList.size()-1){
                        mInfo=0;
                    }else {
                        mInfo++;
                    }
                    mMediaPlayer=MediaPlayer.create(getApplicationContext(),mArrayList.get(mInfo));
                    mMediaPlayer.start();
                    STATUS=true;
                    setNotification(STATUS);
                    Toast.makeText(getApplicationContext(),"NEXT",Toast.LENGTH_SHORT).show();
                    break;
                case MainActivity.PREVIOUS_CODE:
                    Log.i("PlayService:","PREVIOUS");
                    if (mMediaPlayer.isPlaying()){
                        mMediaPlayer.stop();
                    }
                    if (mInfo==0){
                        mInfo=mArrayList.size()-1;
                    }else {
                        mInfo--;
                    }
                    mMediaPlayer=MediaPlayer.create(getApplicationContext(),mArrayList.get(mInfo));
                    mMediaPlayer.start();
                    STATUS=true;
                    setNotification(STATUS);
                    Toast.makeText(getApplicationContext(),"PREVIOUS",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("PlayService :", "onCreate");
        initList();
        mMediaPlayer=MediaPlayer.create(this,mArrayList.get(mInfo));
        notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("PlayService", "start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    //初始化列表
    public void initList(){
        Log.i("PlayService :","Init List");

        mArrayList.add(0, R.raw.holdmyhand);
        mArrayList.add(1, R.raw.keepyourheadup);

        titleArrayList.add(0, "Hold my hand");
        titleArrayList.add(1, "Keep your head up");

        singerArrayList.add(0,"Michael Jackson & Akon");
        singerArrayList.add(1,"Michael Jackson");
    }

    //初始化Notification
    public void initNotification(){
        Log.i("PlayService :", "Init Notification");
        mIntent=new Intent(this,MyRecevier.class);

        remoteViews=new RemoteViews(getPackageName(),R.layout.notifi_view);

        remoteViews.setTextViewText(R.id.title_text_view, "Title");
        remoteViews.setTextViewText(R.id.context_text_view, "Artist");

        if(mMediaPlayer.isPlaying()){
            remoteViews.setImageViewResource(R.id.controll_button, R.drawable.pause);

            mIntent.setAction(NOTIFICATION_PAUSE);
            PendingIntent pendingIntentPlay=PendingIntent.getBroadcast(this, 0, mIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.controll_button, pendingIntentPlay);

        }else {
            remoteViews.setImageViewResource(R.id.controll_button, R.drawable.play);

            mIntent.setAction(NOTIFICATION_PLAY);
            PendingIntent pendingIntentPause=PendingIntent.getBroadcast(this, 0, mIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.controll_button, pendingIntentPause);
        }
        mIntent.setAction(NOTIFICATION_PREVIOUS);
        PendingIntent pendingIntentPre=PendingIntent.getBroadcast(this, 0, mIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.previous_notif_icon, pendingIntentPre);

        mIntent.setAction(NOTIFICATION_NEXT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this, 0, mIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.next_notif_button, pendingIntentNext);

        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, mIntent, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            builder = new Notification.Builder(this)
                    .setContent(remoteViews)
                    .setContentTitle("Title")
                    .setSmallIcon(R.drawable.music_icon)
                    .setContentIntent(pendingIntent)
                    .setContentText("This is a text");
        }
        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(CUSTOM_VIEW_ID, builder.build());
        }
    }

    public void setNotification(Boolean tag){
        remoteViews.setTextViewText(R.id.title_text_view, titleArrayList.get(mInfo));
        remoteViews.setTextViewText(R.id.context_text_view, singerArrayList.get(mInfo));

        if (tag) {
            remoteViews.setImageViewResource(R.id.controll_button, R.drawable.pause);

            mIntent.setAction(NOTIFICATION_PAUSE);
            PendingIntent pendingIntentPlay=PendingIntent.getBroadcast(this, 0, mIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.controll_button, pendingIntentPlay);

        } else {
            remoteViews.setImageViewResource(R.id.controll_button, R.drawable.play);

            mIntent.setAction(NOTIFICATION_PLAY);
            PendingIntent pendingIntentPause=PendingIntent.getBroadcast(this, 0, mIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.controll_button, pendingIntentPause);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            builder = new Notification.Builder(this)
                    .setContent(remoteViews)
                    .setSmallIcon(R.drawable.music_icon);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(CUSTOM_VIEW_ID, builder.build());
        }
    }
}
