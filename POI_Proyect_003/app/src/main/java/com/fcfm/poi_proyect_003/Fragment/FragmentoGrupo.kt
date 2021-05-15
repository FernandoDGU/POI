package com.fcfm.poi_proyect_003.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fcfm.poi_proyect_003.Adaptadores.SubGruposClickListener
import com.fcfm.poi_proyect_003.Adaptadores.subGruposAdapter
import com.fcfm.poi_proyect_003.AltaGruposActivity
import com.fcfm.poi_proyect_003.ChatGrupoActivity
import com.fcfm.poi_proyect_003.Clases.SubGrupos
import com.fcfm.poi_proyect_003.HomeActivity
import com.fcfm.poi_proyect_003.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.group_activity.view.*
import java.lang.Exception
import java.sql.Ref

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


    private val database = FirebaseDatabase.getInstance()
    private val subgruposRef = database.getReference("SubGrupos")
    private val Ref = database.getReference()
    private val SubGrupos = mutableListOf<SubGrupos>()

    private lateinit var adaptadorSubGrupos: subGruposAdapter
    var idGrupo = "1"
    var grupoNombre = ""
    var nombre = ""
    var CarreraUsuario = ""
    var CorreoUsuario = ""

    private lateinit var rootView: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.group_activity, container, false)

        //Modificaciones
        CarreraUsuario = (getActivity() as HomeActivity).getCarrera()
        CorreoUsuario = (getActivity() as HomeActivity).getCorreo()


        adaptadorSubGrupos = subGruposAdapter(this, SubGrupos, object : SubGruposClickListener{

            override fun subGrupoListener(grupos: SubGrupos) {
                idGrupo = grupos.id
                grupoNombre = grupos.nombreGrupo

            }
        })

        rootView.btnCrearGrupo.setOnClickListener {
            val intent = Intent(this@FragmentoGrupo.context, AltaGruposActivity::class.java)
            intent.putExtra("carrera", CarreraUsuario)
            startActivity(intent)
        }



        //Ir al grupo principal
        rootView.btnPreba.setOnClickListener {
            val intent = Intent(this@FragmentoGrupo.context, ChatGrupoActivity::class.java)
            intent.putExtra("Carrera", (getActivity() as HomeActivity).getCarrera())
            startActivity(intent)
        }

       rootView.rvSubGrupos.adapter = adaptadorSubGrupos
        getSubGrupos()
        return rootView
    }

    fun mostrarGrupos(){
        var groupList = ArrayList<SubGrupos>()
        val databaseReference:DatabaseReference =
                FirebaseDatabase.getInstance().getReference("Grupos")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val grupo = dataSnapShot.getValue(SubGrupos::class.java)
                    if(true){
                        if (grupo != null) {
                            //groupList.add(grupo)
                        }
                    }
                }
                //val groupAdapter = Adapter(this, R.layout.group_list,groupList)
                //chatRecyclerView.adapter = chatAdapter
            }
        })

    }


    fun getSubGrupos(){
        try {
            subgruposRef.addValueEventListener(object : ValueEventListener{

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    SubGrupos.clear()
                    var correoPersona = ""
                    for (snap in snapshot.children){
                        var childKey = snap.key.toString()
                        var Personas = snap.child("Participantes").children
                        for (persona in Personas){
                            correoPersona = persona.child("correo").value.toString()
                            var nombrePersona = persona.child("nombre").value.toString()
                            if(correoPersona == CorreoUsuario){
                                var carrera = snap.child("carrera").value.toString()
                                var id = snap.child("id").value.toString()
                                var nombre = snap.child("nombreGrupo").value.toString()

                                if(carrera == CarreraUsuario){
                                    SubGrupos.add(SubGrupos(id, carrera, nombre))
                                }
                            }
                        }

                        if(SubGrupos.size > 0 ){
                            adaptadorSubGrupos.notifyDataSetChanged()
                        }
                    }
                }




            })
            adaptadorSubGrupos.notifyDataSetChanged()
        }catch (e: Exception){
            Log.d("TAG", e.toString())
        }
    }
}
