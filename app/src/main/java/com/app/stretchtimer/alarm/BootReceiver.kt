package com.app.stretchtimer.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.stretchtimer.setTimeAndAlarm

class BootReceiver: BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {
            setTimeAndAlarm(context!!)
        }
    }

}