package com.example.myapplication.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.myapplication.AdapterMensajes
import com.example.myapplication.Entidades.Mensaje
import com.example.myapplication.Entidades.Usuario
import com.example.myapplication.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity() {

    private var fotoperfil: CircleImageView? = null;
    private var nombre: TextView?=null;
    private var rvMensajes: RecyclerView?=null;
    private var txtMensaje: TextView?=null;
    private var btnEnviar: Button?=null;
    private var cerrarSesion: Button?=null;
    private var btnEnviarFoto: ImageButton? = null

    private var adapter: AdapterMensajes?=null;

    private var database: FirebaseDatabase?=null;
    private var databaseReference: DatabaseReference?=null;
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var fotoPerfilCadena: String? = null
    private var NOMBRE_USUARIO: String?= null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fotoperfil = findViewById<View>(R.id.fotoPerfil) as CircleImageView;
        nombre = findViewById<View>(R.id.nombre) as TextView;
        rvMensajes = findViewById<View>(R.id.rvMensajes) as RecyclerView;
        txtMensaje = findViewById<View>(R.id.txtMensaje) as TextView;
        btnEnviar = findViewById<View>(R.id.btnEnviar) as Button;
        btnEnviarFoto = findViewById(R.id.btnEnviarFoto) as ImageButton?
        cerrarSesion = findViewById(R.id.cerrarSesion) as Button;


        database = FirebaseDatabase.getInstance();
        //databaseReference = database!!.getReference("chat") //Sala de chat (nombre)
        databaseReference = database!!.getReference("chatV2") //Sala de chat (nombre) version 2
        storage = FirebaseStorage.getInstance()

        auth = Firebase.auth

        adapter = AdapterMensajes(this);
        val l = LinearLayoutManager(this);
        rvMensajes!!.layoutManager = l;
        rvMensajes!!.adapter = adapter;
        btnEnviar!!.setOnClickListener {
           // adapter!!.addMensaje(Mensaje(txtMensaje!!.text.toString(), nombre_usuario!!.text.toString(), "", "1", "00:00"))
            databaseReference!!.push().setValue(Mensaje(txtMensaje!!.text.toString(), NOMBRE_USUARIO, "", "1", "00:00"));
            txtMensaje!!.setText("");
        }

        cerrarSesion!!.setOnClickListener {
            Firebase.auth.signOut()
            returnLogin()

        }

        btnEnviarFoto!!.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/jpeg"
            i.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_SEND)
        }


        adapter!!.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                setScrollbar()
            }
        });
        databaseReference!!.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                val m : Mensaje = dataSnapshot.getValue(Mensaje::class.java)!!;
                adapter!!.addMensaje(m)

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }


        });
    }

    private fun setScrollbar() {
        rvMensajes!!.scrollToPosition(adapter!!.itemCount - 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            var u = data?.data
            storageReference = storage?.getReference("imagenes_chat") //imagenes_chat
            val fotoReferencia: StorageReference = storageReference!!.child("u.lastPathSegment")
            if (u != null) {
                fotoReferencia.putFile(u).addOnSuccessListener(this, OnSuccessListener { taskSnapshot ->
                    //val u: Uri = taskSnapshot.storage.downloadUrl
                    var u: Task<Uri> = taskSnapshot.storage.downloadUrl
                    val m = Mensaje(NOMBRE_USUARIO+" ha actualizado su foto de perfil", u.toString(), NOMBRE_USUARIO!!, "", "2", "00:00")
                    databaseReference?.push()?.setValue(m)

                })
            }

        }

    }

    override fun onResume() {
        super.onResume()
        val currentUser: FirebaseUser = auth.getCurrentUser()
        if (currentUser != null) {
            btnEnviar!!.isEnabled = false
            val reference: DatabaseReference = database!!.getReference("Usuarios/" + currentUser.getUid())

            reference.addListenerForSingleValueEvent( object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val usuario: Usuario? = dataSnapshot.getValue(Usuario::class.java)

                    NOMBRE_USUARIO = usuario?.nombre
                    nombre!!.text = NOMBRE_USUARIO
                    btnEnviar!!.isEnabled = true

                }

                override fun onCancelled(error: DatabaseError) {}
            })
        } else {
            returnLogin()
        }
    }

    private fun returnLogin() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }
    companion object {
        private const val PHOTO_SEND = 1
        private const val PHOTO_PERFIL = 2
    }

}
