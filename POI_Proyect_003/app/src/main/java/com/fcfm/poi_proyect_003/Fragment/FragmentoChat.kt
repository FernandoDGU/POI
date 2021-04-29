package com.fcfm.poi_proyect_003.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fcfm.poi_proyect_003.Clases.ChatGrupal
import com.fcfm.poi_proyect_003.R
import com.google.firebase.database.FirebaseDatabase

class FragmentoChat: Fragment() {
    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("")
    private val Ref = database.getReference()

    private val listMensajes = mutableListOf<ChatGrupal>()
    private lateinit var rootView: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_chat_grupal, container,  false)
        return rootView
        }
    }
