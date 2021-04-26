package com.fcfm.poi_proyect_003

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    var carrera: String = "LMAD"
    private lateinit var auth: FirebaseAuth //Para autentificacion por correo
    private var database = FirebaseDatabase.getInstance()
    private var userRef = database.getReference("Usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        title = "Registrar Usuario"
        auth = FirebaseAuth.getInstance()

        btnAceptar.setOnClickListener {
            val nombreUsuario = txtRegistroNombre.text.toString()
            val passwordUsuario = txtRegistroPass.text.toString()
            val carreraUsuario = carrera
            val correoUsuario = txtRegistroCorreo.text.toString()
            val estado = "Desconectado"
            MandarDatos(Usuarios(nombreUsuario, passwordUsuario, carreraUsuario, correoUsuario, estado))
        }

    }   private fun MandarDatos(user:Usuarios) {
        auth.createUserWithEmailAndPassword(user.correo, user.contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var nombre = user.nombre
                    var profileupdate = UserProfileChangeRequest.Builder().setDisplayName(nombre).build()
                    FirebaseAuth.getInstance().currentUser?.updateProfile(profileupdate)
                        ?.addOnCompleteListener(this){task2 -> if(!task2.isSuccessful){
                            Toast.makeText(this, "no se pudo poner nombre", Toast.LENGTH_SHORT).show()
                        }else{
                            var usuariodb = userRef.push()
                            var usuario = Usuarios(
                                user.nombre,
                                user.contrasena,
                                user.carrera,
                                user.correo,
                                user.estado
                            )
                            usuariodb.setValue(usuario)
                            Toast.makeText(this, "usuario ingresado correctamente", Toast.LENGTH_SHORT).show()
                            //startActivity(Intent(this, MainActivity::class.java))
                        }
                        }
                } else {
                    Toast.makeText(this, "no se pudo guardar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }
}