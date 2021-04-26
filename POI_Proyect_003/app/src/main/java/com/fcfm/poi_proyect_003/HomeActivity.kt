package com.fcfm.poi_proyect_003

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Fragment.FragmentoGrupo
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnChatGrupal.setOnClickListener {
            startActivity(Intent(this, FragmentoGrupo::class.java))

        }
    }
}