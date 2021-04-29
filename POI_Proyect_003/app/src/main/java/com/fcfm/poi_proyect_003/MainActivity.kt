package com.fcfm.poi_proyect_003

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Conexion a la base de datos
    private val database = FirebaseDatabase.getInstance()
    private val referenciaDb = database.getReference()
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Bienvenido"
        //Obtener los datos del usuario
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null){
            firebaseUser = auth.currentUser!!
            if(firebaseUser!=null){
                val intentHome = Intent(this, HomeActivity::class.java)
                startActivity(intentHome)
                Toast.makeText(this, "Usuario logeado correctamente", Toast.LENGTH_SHORT).show()
            }
        }


        //Boton para abrir el registro de usuarios
        btnRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        //Botono para confirmar login e ir a home
        btnIniciarSesion.setOnClickListener {
            val correoUsuario = txtCorreo.text.toString()
            val passUsuario = txtPass.text.toString()
            Login(correoUsuario, passUsuario)
        }
    }

    private fun Login(correo:String, pass:String){
        auth.signInWithEmailAndPassword(correo, pass).addOnCompleteListener(this){
            task ->if(task.isSuccessful){
            Toast.makeText(this, "Usuario logeado correctamente", Toast.LENGTH_SHORT).show()
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)

        }else{
            Toast.makeText(this, "Error al ingresar, verifica los datos", Toast.LENGTH_SHORT).show()
        }
        }
    }

}