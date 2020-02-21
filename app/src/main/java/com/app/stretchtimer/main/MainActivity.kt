package com.app.stretchtimer.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.app.stretchtimer.*
import com.app.stretchtimer.alarm.Receiver
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var open: Boolean = false
    var wasOffline: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        menu.setOnClickListener {
            drawerToggle()
        }
        setTime()
        switchOffAlarm()
    }

    private fun loadValues() {
        alarmTime.text = getTimeValue(this)
        onoff_switch.isChecked = getSwitchStatus(this)
        wasOffline = !(getSwitchStatus(this))
        changeTextColor()
    }

    fun changeTextColor(){ //changes the color on screen
        if(onoff_switch.isChecked){
            alarm.setTextColor(getResources().getColor(R.color.white))
            alarmTime.setTextColor(getResources().getColor(R.color.white))
        }else{
            alarm.setTextColor(getResources().getColor(R.color.offcolor))
            alarmTime.setTextColor(getResources().getColor(R.color.offcolor))
        }
    }

    fun setTime(){ //set the time the alarm will go off
            alarmTime.setOnClickListener {
                //if(onoff_switch.isChecked){
                    val cal = Calendar.getInstance()
                    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        cal.set(Calendar.SECOND, 0)

                        var millis = cal.timeInMillis
                        if((millis - System.currentTimeMillis()) < 0) {
                            millis += 86400000L
                        }

                        val timeString = SimpleDateFormat("HH:mm").format(cal.time)
                        val isOn = true

                        saveTimeValue(this, timeString)
                        saveSwitchStatus(this, isOn)

                        setTimeValue(timeString)

                        setAlarm(millis)
                    }

                    TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE+1), true).show() //TODO: on the first click it's 12.00, but once it's set it needs to stay the same, no auto update, maybe a test flow and a release flow
                //}
                setTimeAndAlarm(this)
                saveSwitchStatus(true)
                onoff_switch.isChecked = true
                changeTextColor()
            }

    }

    fun switchOffAlarm(){
        onoff_switch.setOnCheckedChangeListener { button, isChecked ->

            changeTextColor()

            if(isChecked){
                setTimeAndAlarm(this)
                saveSwitchStatus(true)
                //toast(this, "alarm is reset")
            }
            if(!isChecked){
                toast(this, "Alarm is turned off")
                cancelAlarm()
                saveSwitchStatus(false)
            }
        }
    }

    fun saveSwitchStatus(bool:Boolean){
        val sp: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val ed: SharedPreferences.Editor = sp.edit()
        ed.putBoolean(SWITCH_STATUS, bool)
        ed.apply()
    }

    fun cancelAlarm(){
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(this, Receiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, i, 0)
        alarmManager.cancel(pendingIntent)
    }

    private fun setTimeValue(timeString: String) {
        alarmTime.text = timeString
        changeTextColor()
    }

    fun setAlarm(timeInMillis:Long){

        val intent = Intent(this, Receiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager?
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

    fun openDrawer() {
        open = true
        drawer_layout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        open = false
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    fun drawerToggle() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            openDrawer()
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
        loadValues()
    }

}
