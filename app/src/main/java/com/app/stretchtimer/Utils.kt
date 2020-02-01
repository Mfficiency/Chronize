package com.app.stretchtimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import com.app.stretchtimer.alarm.Receiver
import java.text.SimpleDateFormat
import java.util.*

fun saveSwitchStatus(context: Context, isOn: Boolean) {
        val sp: SharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        val ed: SharedPreferences.Editor = sp.edit()
        ed.putBoolean(SWITCH_STATUS, isOn)
        ed.apply()
    }

    fun saveTimeValue(context: Context, timeString: String){
        val sp: SharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,
            Context.MODE_PRIVATE
        )
        val ed: SharedPreferences.Editor = sp.edit()
        ed.putString(TIME_VALUE, timeString)
        ed.apply()
    }

    fun getTimeValue(context: Context): String?{
        val sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sp.getString(TIME_VALUE, SimpleDateFormat("HH:mm").format(System.currentTimeMillis()))
    }

    fun getSwitchStatus(context: Context): Boolean{
        val sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sp.getBoolean(SWITCH_STATUS, false)
    }

    fun setTimeAndAlarm(context: Context){
        val timeString = getTimeValue(context)
        val time = timeString!!.split(":")
        val hour = time[0].toInt()
        val minute = time[1].toInt()

        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)

        var millis = cal.timeInMillis

        if((millis - System.currentTimeMillis()) < 0) {
            millis += 86400000L
        }

        setAlarm(context, millis)
    }

    fun setAlarm(context: Context, timeInMillis:Long){

        val intent = Intent(context, Receiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        if(alarmManager == null)
            return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val alarmClockInfo = AlarmManager.AlarmClockInfo(timeInMillis, pendingIntent)
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        }
        else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        }

    }