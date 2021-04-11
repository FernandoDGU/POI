package com.example.myapplication.Activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Entidades.Usuario
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class RegistroActivity : AppCompatActivity() {
    private var txtNombre: EditText? = null
    private var txtCorreo: EditText? = null
    private var txtContraseña: EditText? = null
    private var txtContraseñaRepetida: EditText? = null
    private var btnRegistrar: Button? = null
    private lateinit var auth: FirebaseAuth
    private var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        txtNombre = findViewById<View>(R.id.idRegistroNombre) as EditText
        txtCorreo = findViewById<View>(R.id.idRegistroCorreo) as EditText
        txtContraseña = findViewById<View>(R.id.idRegistroContraseña) as EditText
        txtContraseñaRepetida =
            findViewById<View>(R.id.idRegistroContraseñaRepetida) as EditText
        btnRegistrar =
            findViewById<View>(R.id.idRegistroRegistrar) as Button
        // Initialize Firebase Auth
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        btnRegistrar!!.setOnClickListener {
            val correo = txtCorreo!!.text.toString()
            val nombre = txtNombre!!.text.toString()
            if (isValidEmail(correo) && validarContraseña() && validarNombre(nombre)) {
                val contraseña = txtContraseña!!.text.toString()
                auth!!.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(
                        this@RegistroActivity
                    ) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(
                                this@RegistroActivity,
                                "Se registro correctamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val usuario = Usuario()
                            usuario.correo = correo
                            usuario.nombre = nombre
                            val currentUser = auth!!.currentUser
                            val reference =
                                database?.getReference("Usuarios/" + currentUser.uid)
                            reference?.setValue(usuario)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@RegistroActivity,
                                "Error al registrarse.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this@RegistroActivity,
                    "Validaciones funcionando.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
    }

    fun validarContraseña(): Boolean {
        val contraseña: String
        val contraseñaRepetida: String
        contraseña = txtContraseña!!.text.toString()
        contraseñaRepetida = txtContraseñaRepetida!!.text.toString()
        return if (contraseña == contraseñaRepetida) {
            if (contraseña.length >= 6 && contraseña.length <= 16) {
                true
            } else false
        } else false
    }

    fun validarNombre(nombre: String): Boolean {
        return !nombre.isEmpty()
    }
}
