package com.app.stretchtimer.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.stretchtimer.wasScreenOff

class ScreenReceiver: BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOff = true
        }
    }
}