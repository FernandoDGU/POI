package com.fcfm.poi_proyect_003.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fcfm.poi_proyect_003.AltaTareasActivity
import com.fcfm.poi_proyect_003.ChatActivity
import com.fcfm.poi_proyect_003.MainActivity
import com.fcfm.poi_proyect_003.R
import kotlinx.android.synthetic.main.tareas_activity.view.*

class FragmentoTareas:Fragment() {
    private lateinit var rootView: View
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.tareas_activity, container, false)

        rootView.btncrearTarea.setOnClickListener {
            val intent = Intent(this@FragmentoTareas.context, AltaTareasActivity::class.java)
            startActivity(intent)
        }
        return rootView
    }
}