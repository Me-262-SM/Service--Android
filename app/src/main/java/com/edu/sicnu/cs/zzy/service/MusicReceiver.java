package com.edu.sicnu.cs.zzy.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MusicReceiver extends BroadcastReceiver {
    MainActivity mainActivity;

    public MusicReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String musicName = intent.getStringExtra("name");
        mainActivity.textView.setText(musicName);
    }
}
