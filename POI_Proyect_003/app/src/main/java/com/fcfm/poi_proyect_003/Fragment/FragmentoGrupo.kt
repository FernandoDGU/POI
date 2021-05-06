package com.fcfm.poi_proyect_003.Fragment

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fcfm.poi_proyect_003.Adaptadores.chatGrupalAdapter
import com.fcfm.poi_proyect_003.AltaGruposActivity
import com.fcfm.poi_proyect_003.ChatGrupoActivity
import com.fcfm.poi_proyect_003.Clases.ChatGrupal
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.fcfm.poi_proyect_003.HomeActivity
import com.fcfm.poi_proyect_003.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat_grupal.*
import kotlinx.android.synthetic.main.fragment_chat_grupal.view.*
import kotlinx.android.synthetic.main.group_activity.view.*

class FragmentoGrupo: Fragment() {
    /*private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("SalaChat")
    private val Ref = database.getReference()
    private lateinit var auth: FirebaseAuth

    private val listaMensajes = mutableListOf<ChatGrupal>()
    private val adaptadorMensaje = chatGrupalAdapter(listaMensajes)

    private lateinit var rootView: View

    var CorreoUsuario = ""
    var nombre = ""
    var CarreraUsuario = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_chat_grupal, container,  false)
        rootView.txtNombreGrupo.text = "Chat Principal"

        var user = Firebase.auth.currentUser
        CarreraUsuario = "LMAD"
        CorreoUsuario = user.email

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

        rootView.btnEnviarMensaje.setOnClickListener {
            if(txtEnviarMensaje.text.toString() !== ""){
                var mensaje = txtEnviarMensaje.text.toString()
                enviarMensaje(ChatGrupal("", mensaje, CorreoUsuario, nombre, ServerValue.TIMESTAMP))
                txtEnviarMensaje.text.clear()
            }
        }

        rootView.txtNombreGrupo.text = CarreraUsuario
        rootView.rvChatGrupal.adapter = adaptadorMensaje
        recibirMensajes()

        return rootView
        }

    private fun enviarMensaje(mensaje: ChatGrupal){
        val child = chatRef.child("LMAD").push()
        mensaje.id = child.key ?:""
        child.setValue(mensaje)
    }

    private fun recibirMensajes(){
        chatRef.child(CarreraUsuario).addValueEventListener(object : ValueEventListener {

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
                    rvChatGrupal.smoothScrollToPosition(listaMensajes.size) // -1
                }
            }

        })
        adaptadorMensaje.notifyDataSetChanged()
    }*/

    private lateinit var rootView: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.group_activity, container, false)

        rootView.btnCrearGrupo.setOnClickListener {
            val intent = Intent(this@FragmentoGrupo.context, AltaGruposActivity::class.java)
            startActivity(intent)
        }

        rootView.btnPreba.setOnClickListener {
            val intent = Intent(this@FragmentoGrupo.context, ChatGrupoActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

}
