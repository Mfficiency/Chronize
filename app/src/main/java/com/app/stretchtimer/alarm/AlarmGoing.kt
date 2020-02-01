package com.app.stretchtimer.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.app.stretchtimer.*
import kotlinx.android.synthetic.main.activity_alarm_going.*
import java.io.IOException


class AlarmGoing : AppCompatActivity() {

    private var am: AudioManager? = null
    private var mediaPlayer: MediaPlayer? = null
    private var volume: Int = 0
    private var screenReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_going)

        val intentFilter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        screenReceiver = ScreenReceiver()
        registerReceiver(screenReceiver, intentFilter)

        initialize()

        swipe_btn.setOnStateChangeListener {
            finish()
        }
    }


    fun endAlarm(){
        if(mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        }
        resetVolume()
        saveSwitchStatus(this, false)
        cancelVibration()
    }

    private fun initialize(){
        am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private fun resetVolume(){
        if(am != null){
            am!!.setStreamVolume(AudioManager.STREAM_ALARM,
                volume, 0)
        }
    }

    private fun startAlarm(){

        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer!!.setDataSource(
                this,
                Settings.System.DEFAULT_ALARM_ALERT_URI
            )
            if (Build.VERSION.SDK_INT >= 21) {
                mediaPlayer!!.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
            } else {
                mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_ALARM)
            }
            mediaPlayer!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if(am != null){
            volume = am!!.getStreamVolume(AudioManager.STREAM_ALARM)

            am!!.setStreamVolume(AudioManager.STREAM_ALARM,
                am!!.getStreamMaxVolume(AudioManager.STREAM_ALARM)/2, 0)
        }

        if(mediaPlayer != null) {
            mediaPlayer!!.start()
        }

    }


    private fun loadValues() {
        val sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val timeValue = sp.getString(TIME_VALUE, "12:13")
        alarmTime.text = timeValue
    }

    private fun vibratePhone(){

        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern:LongArray = longArrayOf(1000, 800)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createWaveform(pattern, 0))
            Log.i("vibrate", "Vibrated in O")
        } else {
            v.vibrate(pattern, 0)
            Log.i("vibrate", "Vibrated")
        }
    }

    private fun cancelVibration(){
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.cancel()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        toBeShownOnLockScreen()
    }

    private fun toBeShownOnLockScreen() {

        window.addFlags(
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true)
            setShowWhenLocked(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            )
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause", "on Paused called")
        endAlarm()
    }

    override fun onResume() {
        super.onResume()
        if(wasScreenOff){
            wasScreenOff = false
            finish()
        }
        startAlarm()
        vibratePhone()
        loadValues()
        Log.e("onResume", "on Resumed called"+ wasScreenOff)

    }

    override fun onStop() {
        super.onStop()
        Log.e("onStopped", "on Stoped called"+ wasScreenOff)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenReceiver)
        Log.e("onDestroy", "on Destroy called")
    }
}
