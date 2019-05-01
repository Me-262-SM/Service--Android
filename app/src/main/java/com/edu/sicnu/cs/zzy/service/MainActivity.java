package com.edu.sicnu.cs.zzy.service;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Intent intent;
    MusicReceiver musicReceiver;

    final static String BroadMusic = "com.edu.sicnu.cs.zzy.receiver";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        textView = findViewById(R.id.musicinfo);
        intent = new Intent(MainActivity.this,MyService.class);
        musicReceiver = new MusicReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadMusic);
        registerReceiver(musicReceiver,intentFilter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }
    }

    public void play(View view){
        intent.putExtra("operate",1);
        startService(intent);
    }

    public void stop(View view){
        intent.putExtra("operate",2);
        startService(intent);
    }

    public void pause(View view){
        intent.putExtra("operate",3);
        startService(intent);
    }

    public void next(View view){
        intent.putExtra("operate",4);
        startService(intent);
    }

    public void prev(View view){
        intent.putExtra("operate",5);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(musicReceiver);
    }
}
