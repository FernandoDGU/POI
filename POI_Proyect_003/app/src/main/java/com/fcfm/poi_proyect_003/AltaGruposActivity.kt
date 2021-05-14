package com.fcfm.poi_proyect_003

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Clases.Grupos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.group_form.*



class AltaGruposActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_form)

        val nuevoGrupo: Button = findViewById(R.id.btnAgregarGrupo)
        nuevoGrupo.setOnClickListener{
            val nombreGrupo = etNombreGrupo.text.toString()
            val descGrupo = etDescGrupo.text.toString()
            if(nombreGrupo != "") {
                agregarGrupoBD(nombreGrupo, descGrupo)
            }
            else {
                Toast.makeText(
                        applicationContext,
                        "Ingresa un nombre para el grupo",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun agregarGrupoBD(nombreGrupo:String,descGrupo:String){

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        var reference:DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var grupo:HashMap<String, String> = HashMap()
        grupo.put("nombreGrupo", nombreGrupo)
        grupo.put("descGrupo", descGrupo)
        grupo.put("idCreador", firebaseUser!!.uid)

        reference!!.child("Grupos").push().setValue(grupo)

        finish()

        /*var database: DatabaseReference
        database = FirebaseDatabase.getInstance().reference

        var firebaseUser: FirebaseUser? = null
        var reference: DatabaseReference? = null
        var userId = intent.getStringExtra("idAlmuno")
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId!!)

        database.child("Grupos").setValue(Grupo(nombreGrupo,descGrupo,userId.toString()))*/

    }
}