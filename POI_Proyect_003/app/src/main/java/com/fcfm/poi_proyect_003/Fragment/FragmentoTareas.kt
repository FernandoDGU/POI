package com.fcfm.poi_proyect_003.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcfm.poi_proyect_003.*
import com.fcfm.poi_proyect_003.Adaptadores.TareasAdapter
import com.fcfm.poi_proyect_003.Adaptadores.TareasClickListener
import com.fcfm.poi_proyect_003.Clases.Tareas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.group_activity.view.*
import kotlinx.android.synthetic.main.tareas_activity.view.*

class FragmentoTareas:Fragment() {
    private lateinit var rootView: View
    private val tareaList = mutableListOf<Tareas>()
    private lateinit var adaptadorTarea: TareasAdapter
    private val database = FirebaseDatabase.getInstance()
    private var tareaRef = database.getReference("Tareas")

    var CarreraUsuario: String = ""
    var CorreoUsuario: String = ""


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.tareas_activity, container, false)
        rootView.rvTareas.layoutManager = LinearLayoutManager(this@FragmentoTareas.context, LinearLayout.VERTICAL, false)


        CarreraUsuario = (getActivity() as HomeActivity).getCarrera()
        CorreoUsuario = (getActivity() as HomeActivity).getCorreo()

        rootView.btncrearTarea.setOnClickListener {
            val intent = Intent(this@FragmentoTareas.context, AltaTareasActivity::class.java)
            intent.putExtra("Carrera",CarreraUsuario)
            startActivity(intent)
        }

        //Seleccionar la tarea, ver con detalle
        adaptadorTarea = TareasAdapter(this, tareaList, object : TareasClickListener {
            override fun detalle(Dtarea: Tareas) {
                val intent = Intent(this@FragmentoTareas.context, AltaTareasActivity::class.java)
                intent.putExtra("Ver", "Ver")
                intent.putExtra("id", Dtarea.id)
                intent.putExtra("UserId", Dtarea.userID)
                intent.putExtra("Titulo", Dtarea.Titulo)
                intent.putExtra("Fin", Dtarea.check)
                intent.putExtra("Desc", Dtarea.Desc)
                intent.putExtra("IdGrupo", Dtarea.idSub)
            }

        })

        rootView.rvTareas.adapter = adaptadorTarea
        getTareas()
        return rootView
    }

    fun getTareas(){
        tareaRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tareaList.clear()
                for (snap in snapshot.children){
                    val tarea: Tareas = snap.getValue(Tareas::class.java) as Tareas
                    //Prueba para que obtenga todas las tareas de un solo grupo
                    if(tarea.idSub == CarreraUsuario){
                        tareaList.add(tarea)
                    }
                }
                if(tareaList.size > 0){
                    adaptadorTarea.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        adaptadorTarea.notifyDataSetChanged()
    }
}