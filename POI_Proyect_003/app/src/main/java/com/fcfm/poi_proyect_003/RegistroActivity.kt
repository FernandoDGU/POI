/*package com.fcfm.poi_proyect_003

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

            MandarDatos(nombreUsuario, passwordUsuario, carreraUsuario, correoUsuario, estado)
        }

    }   private fun MandarDatos( nombreUsuario:String, PasswordUsuario:String, carreraUsuario:String, correoUsuario:String, estado:String) {
        auth.createUserWithEmailAndPassword(nombreUsuario, PasswordUsuario)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userC: FirebaseUser? = auth.currentUser
                        val userId:String = userC!!.uid
                        var nombre = nombreUsuario
                        var profileupdate = UserProfileChangeRequest.Builder().setDisplayName(nombre).build()
                        FirebaseAuth.getInstance().currentUser?.updateProfile(profileupdate)
                                ?.addOnCompleteListener(this){task2 -> if(!task2.isSuccessful){
                                    Toast.makeText(this, "no se pudo poner nombre", Toast.LENGTH_SHORT).show()
                                }else{
                                    var usuariodb = userRef.push()
                                    var usuario = Usuarios(
                                            userId,
                                            nombreUsuario,
                                            PasswordUsuario,
                                            carreraUsuario,
                                            correoUsuario,
                                            estado
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
}*/

package com.fcfm.poi_proyect_003


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registro.*

class  RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    var carrera: String = "LMAD"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()

        btnAceptar.setOnClickListener {
            val userName = txtRegistroNombre.text.toString()
            val email = txtRegistroCorreo.text.toString()
            val password = txtRegistroPass.text.toString()
            val carreraUsuario = carrera
            val estado = "Desconectado"
            MandarDatos(userName, password, email, carreraUsuario, estado)

        }



    }

    private fun MandarDatos(userName:String,password:String, email:String, carreraUsuario:String, estado:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val user: FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("idAlumno",userId)
                    hashMap.put("nombre",userName)
                    hashMap.put("contrasena",password)
                    hashMap.put("carrera",carreraUsuario)
                    hashMap.put("estado",estado)
                    hashMap.put("correo",email)

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if (it.isSuccessful){
                            //open home activity
                            txtRegistroNombre.setText("")
                            txtRegistroCorreo.setText("")
                            txtRegistroPass.setText("")
                            val intent = Intent(this@RegistroActivity,
                                HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }
}