package com.fcfm.poi_proyect_003.Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fcfm.poi_proyect_003.*
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_config.*
import kotlinx.android.synthetic.main.activity_config.view.*
import kotlinx.android.synthetic.main.activity_registro.*
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

    var cambioPerfil:Boolean = false
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
                        if(cambioPerfil == true){
                            val imageUri = Uri.parse(usuario.imagen)
                            Picasso.get().load(imageUri).into(imagenRec)
                        }else{
                            val imageUri = Uri.parse(usuario.imagen)
                            Picasso.get().load(imageUri).into(imagenRec)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //Poner los datos
        rootView.ivImageConfig.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"), 123)
        }
        rootView.btnCerrarSession.setOnClickListener {
            val intent = Intent(this@FragmentoConfiguracion.context, MainActivity::class.java)
            startActivity(intent)
            //FirebaseAuth.getInstance().signOut()
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
            FirebaseAuth.getInstance().signOut()

        }
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123 && resultCode == android.app.Activity.RESULT_OK && data!!.data!=null){
            val loadingBar = ProgressDialog(this.context)
            loadingBar.setMessage("Actualizando imagen..")
            loadingBar.show()
            val fileUri = data.data
            val storageRef = FirebaseStorage.getInstance().reference.child("imagenesUsuarios")
            val ref = FirebaseDatabase.getInstance().reference
            val idMessage = ref.push().key
            val filePath = storageRef.child("$idMessage.jgp")

            var uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{
                    task ->
                if(!task.isSuccessful)
                {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener{
                    task ->
                if (task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()
                    val imagen = rootView.ivImageConfig
                    val imageUri = Uri.parse(url)
                    Picasso.get().load(imageUri).into(imagen)

                    chatRef.child("Usuarios").orderByChild("correo").equalTo(correo)
                        .addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (snap in snapshot.children){
                                    val key = snap.key.toString()
                                    if(i == "0"){
                                        chatRef.child("Usuarios").child(key).child("imagen").setValue(url)
                                        i = "1"
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                    //auth.signOut()
                    FirebaseAuth.getInstance().signOut()

                    Toast.makeText(this.context, "Imagen cambiada con exito", Toast.LENGTH_SHORT)

                    loadingBar.hide()
                }
            }
        }
    }
}