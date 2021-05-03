package com.fcfm.poi_proyect_003.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcfm.poi_proyect_003.Adaptadores.UsuariosAdapter
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.fcfm.poi_proyect_003.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat2.view.*
import kotlinx.android.synthetic.main.activity_usuarios.*

class FragmentoChat: Fragment() {
    var userList = ArrayList<Usuarios>()

    //private val database = FirebaseDatabase.getInstance()
    //private val chatRef = database.getReference("")
    //private val Ref = database.getReference()

    //private val listMensajes = mutableListOf<ChatGrupal>()
    private lateinit var rootView: View

    @SuppressLint("WrongConstant")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.activity_usuarios, container,  false)
        //Checar
        /*userRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayout.VERTICAL, false)*/

        /*rootView.imgBack.setOnClickListener{
            //onBackPressed()
        }*/
        return rootView
        }

        /*fun getUserList(){
            val firebase = FirebaseAuth.getInstance().currentUser!!
            val databaseReference: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("Usuarios")

            databaseReference.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(context.applicationContext, error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    val currentUser = snapshot.getValue(Usuarios::class.java)
                    for(dataSnapShot in snapshot.children){
                        val user = dataSnapShot.getValue(Usuarios::class.java)
                        if(user!!.idAlumno != firebase.uid){
                            userList.add(user)
                        }else{
                            // userNameContactos.text = user!!.nombre
                        }
                        //val usuariosAdapter = UsuariosAdapter(this@FragmentoChat, userList)
                        //userRecyclerView.adapter = usuariosAdapter
                    }
                }
            })
        }*/

}
