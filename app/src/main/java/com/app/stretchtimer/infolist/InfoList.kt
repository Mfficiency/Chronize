package com.app.stretchtimer.infolist

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.stretchtimer.R
import kotlinx.android.synthetic.main.activity_info_list.*
import kotlinx.android.synthetic.main.custom_list.view.*

class InfoList : AppCompatActivity() {

    val ver = arrayOf<String>("V 0.2", "V 0.1")
    val test = arrayOf<String>("Self-test\nUnderstanding the inner workings", "Testing all systems")
    val infos = arrayOf<String>("https://play.google.com/store/apps/details?id=com.app.stretchtimer","- Home screen\n- Menu\n- Info")
    val dev = arrayOf<String>("Update by me", "- Powerd by Saifal")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_list)

        back.setOnClickListener {
            finish()
        }
        val adapter = CustomListAdapter(
            this,
            ver,
            test,
            infos,
            dev
        )
        listView.adapter = adapter
    }

   class CustomListAdapter(private val context: Activity, private val ver: Array<String>,
                           private val test: Array<String>,
   private val infos: Array<String>, private val dev: Array<String>): ArrayAdapter<String>(context,
       R.layout.custom_list, ver){
       override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
           val rowView = context.layoutInflater.inflate(R.layout.custom_list, null, true)
           rowView.ver.text = ver[position]
           rowView.test.text = test[position]
           rowView.infos.text = infos[position]
           rowView.dev.text = dev[position]
           return rowView
       }
   }
}

