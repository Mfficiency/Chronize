package com.app.stretchtimer.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Receiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

//        mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
//
//        val am = context!!.getSystemService(AUDIO_SERVICE) as AudioManager
//
//        mediaVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
//
//        am.setStreamVolume(AudioManager.STREAM_MUSIC,
//            am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2, 0)
//
//        mediaPlayer.start()

        val intent = Intent(context, AlarmGoing::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(intent)

    }

}