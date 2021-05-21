package com.fcfm.poi_proyect_003

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Adaptadores.chatGrupalAdapter
import com.fcfm.poi_proyect_003.Clases.ChatGrupal
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.fcfm.poi_proyect_003.Fragment.FragmentoGrupo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_chat_grupal.*
import kotlinx.android.synthetic.main.mensaje.*
import java.util.concurrent.locks.Condition
import kotlin.math.log
import android.app.Activity as A

class ChatGrupoActivity: AppCompatActivity(){
    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("SalaChat")
    private val Ref = database.getReference()
    private lateinit var auth: FirebaseAuth

    private val listaMensajes  = mutableListOf<ChatGrupal>()
    private val adaptadorMensaje = chatGrupalAdapter(listaMensajes)


    var nombre = ""
    var agregar = ""


    //Datos recibidos
    var idGrupo : String = ""
    var CorreoUsuario = ""
    var CarreraUsuario : String = ""
    var NombreGrupo: String = ""
    var condition: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_grupal)
        var firebaseUser: FirebaseUser? = null
        var reference:DatabaseReference? = null
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        //Checar esto para obtener el correo del usuario logeado



        //Checar porque se ve de ese tamaño los mensajes
        var intent = getIntent()
        var user = Firebase.auth.currentUser
        CorreoUsuario = user.email
        CarreraUsuario = intent.getStringExtra("Carrera").toString()
        NombreGrupo = intent.getStringExtra("nombre").toString()
        agregar = intent.getStringExtra("Agregar").toString()
        idGrupo = intent.getStringExtra("id").toString()

        if(agregar == "AgregarUsuario"){
            btnAgregarUsuario.visibility = android.view.View.VISIBLE
            //CarreraUsuario = intent.getStringExtra("nombre").toString()
            txtNombreGrupo.text = intent.getStringExtra("nombre").toString()
            condition = true

        }else{
            condition = false
            txtNombreGrupo.text = intent.getStringExtra("Carrera").toString()
        }



        Ref.child("Usuarios").orderByChild("correo").equalTo(CorreoUsuario).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children){
                    val usuario = snap.getValue(Usuarios::class.java) as Usuarios
                    nombre = usuario.nombre
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        //SEGUNDO
        btnAgregarUsuario.setOnClickListener {
            val intent = Intent(this, AgregarSubActivity::class.java)
            intent.putExtra("Carrera", CarreraUsuario)
            intent.putExtra("id", idGrupo)
            intent.putExtra("Correo", CorreoUsuario)
            startActivity(intent)
        }
            //val intent = Intent(this@FragmentoGrupo.context, AltaGruposActivity::class.java)
            //intent.putExtra("carrera", CarreraUsuario)
            //startActivity(intent)


        btnEnviarMensaje.setOnClickListener {
            if(condition == true){
                if (txtEnviarMensaje.text.toString() !== "") {
                    var mensaje = txtEnviarMensaje.text.toString()
                    enviarMensajeSub(
                        ChatGrupal(
                            "",
                            mensaje,
                            CorreoUsuario,
                            nombre,
                            ServerValue.TIMESTAMP
                        )
                    )
                    txtEnviarMensaje.text.clear()
                }
            }else {
                if (txtEnviarMensaje.text.toString() !== "") {
                    var mensaje = txtEnviarMensaje.text.toString()
                    enviarMensaje(
                        ChatGrupal(
                            "",
                            mensaje,
                            CorreoUsuario,
                            nombre,
                            ServerValue.TIMESTAMP
                        )
                    )
                    txtEnviarMensaje.text.clear()
                }
            }

        }


        if(condition == true){
            rvChatGrupal.adapter = adaptadorMensaje
            recibirMensajeSub()
        }else{
            rvChatGrupal.adapter = adaptadorMensaje
            recibirMensaje()
        }


    }

    private fun enviarMensaje(mensaje: ChatGrupal){
        //CARRERA USUARIO ES EL NOMBRE DEL GRUPO
        val child = chatRef.child(CarreraUsuario).push()
        mensaje.id = child.key ?:""
        child.setValue(mensaje)
    }
    private fun enviarMensajeSub(mensaje: ChatGrupal){
        //CARRERA USUARIO ES EL NOMBRE DEL GRUPO
        val child = chatRef.child(NombreGrupo).push()
        mensaje.id = child.key ?:""
        child.setValue(mensaje)
    }

    private fun recibirMensaje(){
        //CARRERA USUARIO ES EL NOMBRE DEL GRUPO
        chatRef.child(CarreraUsuario).addValueEventListener(object : ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listaMensajes.clear()
                for(snap in snapshot.children){
                    val mensaje : ChatGrupal = snap.getValue(ChatGrupal::class.java) as ChatGrupal

                    if(mensaje.de == CorreoUsuario){
                        mensaje.esMio = true
                    }
                    listaMensajes.add(mensaje)
                }
                if(listaMensajes.size>0){
                    adaptadorMensaje.notifyDataSetChanged()
                    rvChatGrupal.smoothScrollToPosition(listaMensajes.size-1)
                }
            }

        })
        adaptadorMensaje.notifyDataSetChanged()
    }

    private fun recibirMensajeSub(){
        //CARRERA USUARIO ES EL NOMBRE DEL GRUPO
        chatRef.child(NombreGrupo).addValueEventListener(object : ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listaMensajes.clear()
                for(snap in snapshot.children){
                    val mensaje : ChatGrupal = snap.getValue(ChatGrupal::class.java) as ChatGrupal

                    if(mensaje.de == CorreoUsuario){
                        mensaje.esMio = true
                    }
                    listaMensajes.add(mensaje)
                }
                if(listaMensajes.size>0){
                    adaptadorMensaje.notifyDataSetChanged()
                    rvChatGrupal.smoothScrollToPosition(listaMensajes.size-1)
                }
            }

        })
        adaptadorMensaje.notifyDataSetChanged()
    }

}