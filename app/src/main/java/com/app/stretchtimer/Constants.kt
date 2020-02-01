package com.app.stretchtimer

import android.content.Context
import android.widget.Toast

val SHARED_PREF_NAME = "shared_pref"
val TIME_VALUE = "time_value"
val SWITCH_STATUS = "switch_status"


fun toast(context: Context, text: String){
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}