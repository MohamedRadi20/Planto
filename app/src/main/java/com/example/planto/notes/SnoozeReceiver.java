package com.example.planto.notes;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SnoozeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equalsIgnoreCase("com.akash.SnoozeReceiver")) {
//        Bundle b1=intent.getExtras();
//        assert b1 != null;
//        MediaPlayer mediaPlayer=b1.getParcelable("AlarmSongSnooze");
//        assert mediaPlayer != null;
//        mediaPlayer.stop();

            System.out.println("Helllo from snooze receive");
            MusicControl.getInstance(context).stopMusic();
        }
    }
}
