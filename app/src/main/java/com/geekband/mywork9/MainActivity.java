package com.geekband.mywork9;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton nextImageButton;
    private ImageButton previousImageButton;
    private ImageButton controllImageButton;
    private ListView listview;

    private MusicPlayerApp app;
    private insideRecevier localRecevier;
    private IntentFilter intentFilter;

    private int flag = R.drawable.play;
    private String TAG=MainActivity.class.getSimpleName();

    public static final String Filter="com.geekband.MainActivity.insideRecevier.LOCAL_BROADCASTRECEVIER";
    public static final int PLAY_CODE=666;
    public static final int PAUSE_CODE=444;
    public static final int NEXT_CODE=111;
    public static final int PREVIOUS_CODE=888;

    private Messenger mMessenger=null;
    private MusicPlayerApp application;
    private Message message;
    private ArrayList<SongInfo> songInfos=new ArrayList<SongInfo>();

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger=null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "create");
        super.onCreate(savedInstanceState);
        application=(MusicPlayerApp)getApplication();
        setContentView(R.layout.activity_main);
        findViews();
        initSongs();
        SongAdapter adapter=new SongAdapter(MainActivity.this,R.layout.song_item,songInfos);
        listview.setAdapter(adapter);
        registerLocalBroadcastRecevier();
        clickMethod();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(MainActivity.this, PlayService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(serviceConnection!=null)
        {
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application.locaBroadcastManager.unregisterReceiver(localRecevier);
    }

    private void registerLocalBroadcastRecevier() {
        intentFilter=new IntentFilter();
        application.locaBroadcastManager= LocalBroadcastManager.getInstance(this);
        intentFilter.addAction(Filter);
        localRecevier=new insideRecevier();
        application.locaBroadcastManager.registerReceiver(localRecevier, intentFilter);
    }

    public void initSongs() {
        SongInfo s1=new SongInfo("Hold my hand","Michael Jackson & Akon");
        SongInfo s2=new SongInfo("Keep your head up","Michael Jackson");
        songInfos.add(s1);
        songInfos.add(s2);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.controll_button:
                if (flag==R.drawable.play){
                    controllImageButton.setImageResource(R.drawable.pause);
                    message=Message.obtain(null,PLAY_CODE);
                    try {
                        mMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    flag=R.drawable.pause;
                }else if(flag==R.drawable.pause){
                    controllImageButton.setImageResource(R.drawable.play);
                    message=Message.obtain(null,PAUSE_CODE);
                    try {
                        mMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    flag=R.drawable.play;
                }
                break;

            case R.id.next_button:
                controllImageButton.setImageResource(R.drawable.pause);
                message=Message.obtain(null,NEXT_CODE);
                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                flag=R.drawable.pause;
                break;

            case R.id.previous_button:
                controllImageButton.setImageResource(R.drawable.pause);
                message = Message.obtain(null, PREVIOUS_CODE);
                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                flag=R.drawable.pause;
                break;
        }
    }

    public void clickMethod() {
        controllImageButton.setOnClickListener(this);
        previousImageButton.setOnClickListener(this);
        nextImageButton.setOnClickListener(this);
    }

    public void findViews() {
        listview= (ListView) findViewById(R.id.song_list_view);
        controllImageButton= (ImageButton) findViewById(R.id.controll_button);
        nextImageButton= (ImageButton) findViewById(R.id.next_button);
        previousImageButton= (ImageButton) findViewById(R.id.previous_button);
    }

    public class insideRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String value=intent.getAction();
            String flag=intent.getStringExtra(MyRecevier.FLAG);
            Log.i(TAG, value+","+flag);
            if (TextUtils.equals(flag, "play")){
                controllImageButton.setImageResource(R.drawable.pause);
                message=Message.obtain(null,PLAY_CODE);
                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }else if (TextUtils.equals(flag,"pause")){
                controllImageButton.setImageResource(R.drawable.play);
                message=Message.obtain(null,PAUSE_CODE);
                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }else if(TextUtils.equals(flag,"next")){
                controllImageButton.setImageResource(R.drawable.pause);
                message=Message.obtain(null,NEXT_CODE);
                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else if (TextUtils.equals(flag,"previous")){
                controllImageButton.setImageResource(R.drawable.pause);
                message = Message.obtain(null, PREVIOUS_CODE);
                try {
                    mMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
