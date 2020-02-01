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

    val ver = arrayOf<String>("V 0.1", "V 0.2")
    val test = arrayOf<String>("Testing all systems", "Checking ths system")
    val homes = arrayOf<String>("- Home 1", "- Home 2")
    val menus = arrayOf<String>("- Menu 1", "- Menu 2")
    val infos = arrayOf<String>("- Info 1", "- Info 2")
    val dev = arrayOf<String>("- Powerd by Saifal", "")


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
            homes,
            menus,
            infos,
            dev
        )
        listView.adapter = adapter
    }

   class CustomListAdapter(private val context: Activity, private val ver: Array<String>,
                           private val test: Array<String>, private val homes: Array<String>,
                           private val menus: Array<String>,
   private val infos: Array<String>, private val dev: Array<String>): ArrayAdapter<String>(context,
       R.layout.custom_list, ver){
       override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
           val rowView = context.layoutInflater.inflate(R.layout.custom_list, null, true)
           rowView.ver.text = ver[position]
           rowView.test.text = test[position]
           rowView.hom.text = homes[position]
           rowView.menus.text = menus[position]
           rowView.infos.text = infos[position]
           rowView.dev.text = dev[position]
           return rowView
       }
   }
}

