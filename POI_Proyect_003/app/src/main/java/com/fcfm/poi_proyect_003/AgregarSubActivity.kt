package com.fcfm.poi_proyect_003

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcfm.poi_proyect_003.Adaptadores.AlumnosAdapter
import com.fcfm.poi_proyect_003.Adaptadores.UsuarioListener
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat_grupal.*
import kotlinx.android.synthetic.main.group_activity.view.*
import kotlinx.android.synthetic.main.miembros_activity.*


//Es para agregar a un alumno al subgrupo, que el nombre no confunda xd
class AgregarSubActivity : AppCompatActivity(){
    private val database = FirebaseDatabase.getInstance()
    private val Ref = database.getReference()
    private lateinit var adapadorAddUsuarios: AlumnosAdapter
    private val userList = mutableListOf<Usuarios>()

    var carreraUser = ""
    var idGrupo = ""
    var correoUser = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.miembros_activity)

    //TERCERO
        //rvAlumnos.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rvAlumnos.layoutManager = LinearLayoutManager(this@AgregarSubActivity, LinearLayout.VERTICAL, false)
        //Validar datos que no esten vacios
        if(intent.hasExtra("Carrera")){
            carreraUser = intent.getStringExtra("Carrera").toString()
        }
        if(intent.hasExtra("Correo")){
            correoUser = intent.getStringExtra("Correo").toString()
        }
        if (intent.hasExtra("id")){
            idGrupo = intent.getStringExtra("id").toString()
        }

        //Adaptador
        adapadorAddUsuarios = AlumnosAdapter(this, userList, object : UsuarioListener {
            override fun usuarioListener(usuarios: Usuarios) {
                txtNombreShowAdd.text = usuarios.nombre
                txtCorreoShowAdd.text = usuarios.correo
            }

        })

        //Agregar alumno
        btnAgregarMiembro.setOnClickListener{
            agregarAlumno(Usuarios( "",txtNombreShowAdd.text.toString(), "",
                carreraUser, txtCorreoShowAdd.text.toString(), ""))
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        cargarUsuarios(carreraUser)
        rvAlumnos.adapter = adapadorAddUsuarios

    }

    private fun agregarAlumno(usuarios: Usuarios){
            val alumno = Ref.child("SubGrupos").child(idGrupo).child("Participantes").push()
            alumno.setValue(usuarios)
    }

    private fun cargarUsuarios (carrera: String){
        Ref.child("Usuarios").orderByChild("carrera").equalTo(carrera).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for (snap in snapshot.children){
                        var ingresa = true
                        val user = snap.getValue(Usuarios::class.java) as Usuarios

                        Ref.child("SubGrupos").child(idGrupo).addValueEventListener(
                            object : ValueEventListener{
                                override fun onDataChange(snapShot: DataSnapshot) {
                                    var correoUser = ""
                                    var Alumno = snapShot.child("Participantes").children

                                    ingresa = true
                                    for(alumno in Alumno){
                                        correoUser = alumno.child("correo").value.toString()
                                        if(correoUser == user.correo){
                                            ingresa = false
                                        }
                                    }

                                    if(ingresa == true){
                                        userList.add(user)
                                    }

                                    if(userList.size > 0){
                                        adapadorAddUsuarios.notifyDataSetChanged()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@AgregarSubActivity, "Algo mal", Toast.LENGTH_SHORT).show()
                                }

                            }
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        adapadorAddUsuarios.notifyDataSetChanged()
    }

}