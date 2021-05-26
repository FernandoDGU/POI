package com.fcfm.poi_proyect_003

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcfm.poi_proyect_003.Adaptadores.UsuariosAdapter
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_usuarios.*

class UsuariosActivity : AppCompatActivity() {
    var userList = ArrayList<Usuarios>()
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuarios)
        userRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        /*imgBack.setOnClickListener {
            onBackPressed()
        }*/



        getUsersList()
    }

     fun getUsersList(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference:DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Usuarios")

        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(Usuarios::class.java)
                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(Usuarios::class.java)
                    if(user!!.idAlumno != firebase.uid){
                        userList.add(user)
                    }else{
                       // userNameContactos.text = user!!.nombre
                    }
                }
                //val usuariosAdapter = UsuariosAdapter(this@UsuariosActivity, userList)

                //userRecyclerView.adapter = usuariosAdapter
            }

        })
    }
}