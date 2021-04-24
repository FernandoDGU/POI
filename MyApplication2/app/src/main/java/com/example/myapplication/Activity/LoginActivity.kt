package com.example.myapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Activity.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var txtCorreo: EditText? = null
    private var txtContraseña: EditText? = null
    private var btnLogin: Button? = null
    private var btnRegistro: Button? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        this.setTitle("Inicio de sesion")
        txtCorreo = findViewById<View>(R.id.idCorreoLogin) as EditText
        txtContraseña = findViewById<View>(R.id.idContraseñaLogin) as EditText
        btnLogin = findViewById<View>(R.id.idLoginLogin) as Button
        btnRegistro =
            findViewById<View>(R.id.idRegistroLogin) as Button
        auth = Firebase.auth
        btnLogin!!.setOnClickListener {
            val correo = txtCorreo!!.text.toString()
            if (isValidEmail(correo) && validarContraseña()) {
                val contraseña = txtContraseña!!.text.toString()
                auth!!.signInWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(
                        this@LoginActivity
                    ) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(
                                this@LoginActivity,
                                "Se logeo correctamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            nextActivity()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@LoginActivity,
                                "Error, credenciales incorrectas.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Validaciones funcionando.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btnRegistro!!.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    RegistroActivity::class.java
                )
            )
        }
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches()
    }

    fun validarContraseña(): Boolean {
        val contraseña: String
        contraseña = txtContraseña!!.text.toString()
        return if (contraseña.length >= 6 && contraseña.length <= 16) {
            true
        } else false
    }

    override fun onResume() {
        super.onResume()
        val currentUser = auth!!.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "Usuario logeado.", Toast.LENGTH_SHORT).show()
            nextActivity()
        }
    }

    private fun nextActivity() {
        startActivity(Intent(this@LoginActivity, MenuActivity::class.java))
        finish()
    }
}