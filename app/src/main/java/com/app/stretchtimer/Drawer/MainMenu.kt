package com.app.stretchtimer.Drawer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.stretchtimer.infolist.InfoList
import com.app.stretchtimer.R
import kotlinx.android.synthetic.main.main_menu.*

class MainMenu: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_menu, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        information.setOnClickListener {
            val intent = Intent(activity, InfoList::class.java)
            startActivity(intent)
        }

    }
}