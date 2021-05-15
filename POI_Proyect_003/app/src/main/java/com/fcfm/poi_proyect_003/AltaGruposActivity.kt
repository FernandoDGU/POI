package com.fcfm.poi_proyect_003

import android.os.Bundle
import android.renderscript.Sampler
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Clases.SubGrupos
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.group_form.*
import kotlinx.android.synthetic.main.mensaje.*


class AltaGruposActivity: AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val subRef = database.getReference("SubGrupos")
    private val Ref = database.getReference()

    var carreraUser = ""
    var correoUser = ""

    //Variables para guardar los datos del usuario
    var nombreU: String = ""
    var carreraU: String = ""
    var correoU: String = ""
    var idU: String = ""
    var estadoU: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_form)

        //Se obtiene la carrera para saber a cual pertenece el subgrupo
        if(intent.hasExtra("carrera")){
            carreraUser = intent.getStringExtra("carrera").toString()
        }

        var user = Firebase.auth.currentUser
        user?.let {
            //Obtiene el correo del usuario para la busqueda
            var email = user.email
            if(email != null){
                correoUser = email
            }

        }

        //Busqueda del usuario en base al correo para obtener todos sus datos
        Ref.child("Usuarios").orderByChild("correo").equalTo(correoUser)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children){
                        val user = snap.getValue(Usuarios::class.java) as Usuarios
                            nombreU = user.nombre
                            carreraU = user.carrera
                            correoU = user.correo
                            idU = user.idAlumno
                            estadoU = user.estado

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })


        //Botton para agregar un grupo
        val nuevoGrupo: Button = findViewById(R.id.btnAgregarGrupo)
        nuevoGrupo.setOnClickListener{
            val nombreGrupo = etNombreGrupo.text.toString()
            val descGrupo = etDescGrupo.text.toString()
            if(nombreGrupo != "") {
                //agregarGrupoBD(nombreGrupo, descGrupo)
                crearGrupo(SubGrupos("", carreraUser, nombreGrupo), Usuarios(idU, nombreU, "", carreraU, correoU, estadoU))
                Toast.makeText(applicationContext, "Grupo creado con exito", Toast.LENGTH_SHORT).show()
                finish()
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

    private fun crearGrupo(subGrupos: SubGrupos, usuarios: Usuarios){
        val mensajeFirebase = subRef.push()
        val Hello: String = "Hello"
        subGrupos.id = mensajeFirebase.key ?: "" //Puede ser nulo
        subGrupos.imagen = Hello.toString()
        mensajeFirebase.setValue(subGrupos)

        val child = subRef.child(subGrupos.id).
        child("Participantes").push()

        child.setValue(usuarios)

    }

}