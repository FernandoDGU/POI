package com.fcfm.poi_proyect_003

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Clases.Tareas
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.mensaje.*
import kotlinx.android.synthetic.main.tareas_form.*

class AltaTareasActivity: AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private var tareaRef = database.getReference("Tareas")
    private lateinit var tarea: Tareas

    var carreraUser: String = ""
    var verTarea: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tareas_form)


        //Obtener la carrera
        if(intent.hasExtra("Carrera")){
            carreraUser = intent.getStringExtra("Carrera").toString()
        }

        if(intent.hasExtra("Ver")){
            verTarea = intent.getStringExtra("Ver").toString()
        }

        if(verTarea == "Ver"){

            tarea = Tareas(
                intent.getStringExtra("id")!!,
                intent.getStringExtra("UserId")!!,
                intent.getStringExtra("Titulo")!!,
                intent.getStringExtra("Desc")!!,
                intent.getBooleanExtra("Fin", false),
                intent.getStringExtra("IdGrupo")!!
            )
            txtTituloTarea.setText(tarea.Titulo)
            txtDescripcionTarea.setText(tarea.Desc)

            btnAddTarea.visibility = android.view.View.GONE
            btnImagenTarea.visibility = android.view.View.GONE
            txtTituloTarea.isEnabled = false
            txtDescripcionTarea.isEnabled = false
        }

        btnAddTarea.setOnClickListener{
            val titulo = txtTituloTarea.text.toString()
            val desc = txtDescripcionTarea.text.toString()
            var fin = false
            agregarTarea(Tareas("", FirebaseAuth.getInstance().currentUser?.uid.toString(),
            titulo, desc, fin, carreraUser))
            finish()
        }
    }

    private fun agregarTarea(ptarea: Tareas){
        val work = tareaRef.push()
        ptarea.id = tareaRef.key ?: ""
        work.setValue(ptarea)
    }
}