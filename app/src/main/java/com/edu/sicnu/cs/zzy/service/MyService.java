package com.edu.sicnu.cs.zzy.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MyService extends Service implements MediaPlayer.OnPreparedListener {
    int current = 0;
    NotificationCompat.Builder builder;
    ArrayList<Music> musiclist = new ArrayList<>();
    MediaPlayer mediaPlayer;
    final static String myChannel = "mymusic";


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        getMusiclist();
        sendNotification();
        playNew();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int operate = intent.getIntExtra("operate",1);
        switch (operate){
            case 1:
                play();
                break;
            case 2:
                stop();
                break;
            case 3:
                pause();
                break;
            case 4:
                next();
                break;
            case 5:
                prev();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    //设置mediaplayer的回调方法
    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();

        Intent intent = new Intent(MainActivity.BroadMusic);
        intent.putExtra("name",musiclist.get(current).getMusicName());
        sendBroadcast(intent);

        builder.setContentText("music:"+musiclist.get(current).getMusicName());
        startForeground(1,builder.build());
    }

    public void sendNotification(){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //判断安卓版本是否大于API-26
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(myChannel,"my music notification", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this,myChannel);
            builder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle("MyMusicDemo:");

            //设置延时意图，点击通知进入界面
            Intent intent = new Intent(this,MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            startForeground(1,builder.build());
        }
    }

    public void getMusiclist(){
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        while(cursor.moveToNext()){
            String musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String musicPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            musiclist.add(new Music(musicName,musicPath));
        }
    }

    private void playNew() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musiclist.get(current).getMusicPath());
            mediaPlayer.prepareAsync(); //异步调用，防止阻塞
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void play(){
        playNew();
    }

    private void stop(){
        if(mediaPlayer.isPlaying()) mediaPlayer.stop();
    }

    private void pause(){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void next(){
        current++;
        current = current % musiclist.size();
        playNew();
    }

    private void prev(){
        current--;
        if(current<0) current = musiclist.size()-1;
        playNew();
    }
}
