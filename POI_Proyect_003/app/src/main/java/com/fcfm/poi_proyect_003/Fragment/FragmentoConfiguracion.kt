package com.fcfm.poi_proyect_003.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fcfm.poi_proyect_003.*
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_config.*
import kotlinx.android.synthetic.main.activity_config.view.*
import kotlinx.android.synthetic.main.group_list.view.*

class FragmentoConfiguracion: Fragment() {

    private lateinit var rootView: View
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference()

    var correo = ""
    var i = "0"
    var nombre = ""
    var Foto = ""
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.activity_config, container, false)


        var user  = Firebase.auth.currentUser
        var email = user?.email
            if(email != null){
                correo = email
            }

        //Traer los datos
        chatRef.child("Usuarios").orderByChild("correo").equalTo(correo)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children){
                        val key = snap.key.toString()
                        val usuario = snap.getValue(Usuarios::class.java) as Usuarios
                        rootView.txtNombreConfig.text = usuario.nombre
                        val imagenRec = rootView.ivImageConfig
                        val imageUri = Uri.parse(usuario.imagen)
                        Picasso.get().load(imageUri).into(imagenRec)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //Poner los datos

        //val imagenRec = ivImageConfig
        //val imageUri = Uri.parse(Foto)
        //Picasso.get().load(imageUri).into(imagenRec)


        rootView.btnCerrarSession.setOnClickListener {
            val intent = Intent(this@FragmentoConfiguracion.context, MainActivity::class.java)
            startActivity(intent)

            FirebaseAuth.getInstance().signOut()
            //var auth = FirebaseAuth.getInstance()



            chatRef.child("Usuarios").orderByChild("correo").equalTo(correo)
                    .addValueEventListener(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snap in snapshot.children){
                                val key = snap.key.toString()
                                if(i == "0"){
                                    chatRef.child("Usuarios").child(key).child("estado").setValue("Desconectado")
                                    i = "1"
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

            //auth.signOut()


        }

        return rootView

    }
}