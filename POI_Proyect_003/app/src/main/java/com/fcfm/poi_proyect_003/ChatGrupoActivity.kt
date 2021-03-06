package com.fcfm.poi_proyect_003

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.fcfm.poi_proyect_003.Adaptadores.chatGrupalAdapter
import com.fcfm.poi_proyect_003.Clases.ChatGrupal
import com.fcfm.poi_proyect_003.Clases.Tareas
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.fcfm.poi_proyect_003.Fragment.FragmentoGrupo
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_chat_grupal.*
import kotlinx.android.synthetic.main.mensaje.*
import java.io.File
import java.util.concurrent.locks.Condition
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.math.log
import android.app.Activity as A

class ChatGrupoActivity: AppCompatActivity(){
    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("SalaChat")
    private val Ref = database.getReference()
    private lateinit var auth: FirebaseAuth
    private val storageRef = FirebaseStorage.getInstance().reference

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
    var encryptCondition:Boolean = false
    var entro:Boolean = false


    //Cifrado de mensajes
    private val CIPHER_TRANSFORMS = "AES/CBC/PKCS5PADDING"

    //Prueba imagenes
    var imagenPath = ""
    var imagenUrl = ""

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


        //Aquí va la encriptacion
        btnEnviarMensaje.setOnClickListener {

            if(condition == true){
                if (txtEnviarMensaje.text.toString() !== "") {
                    var mensaje = txtEnviarMensaje.text.toString()
                    val mensajeEncrypted = cifrar(mensaje, "FER12345")
                    if(encryptCondition == true){
                        enviarMensajeSub(
                            ChatGrupal("", mensajeEncrypted, CorreoUsuario, nombre, ServerValue.TIMESTAMP, false, ""), true)
                    }else{
                        enviarMensajeSub(
                            ChatGrupal("", mensaje, CorreoUsuario, nombre, ServerValue.TIMESTAMP, false, ""), false)
                    }
                    txtEnviarMensaje.text.clear()
                }
            }else {
                if (txtEnviarMensaje.text.toString() !== "") {
                    var mensaje = txtEnviarMensaje.text.toString()
                    val mensajeEncrypted = cifrar(mensaje, "FER12345")
                    if(encryptCondition == true){
                        enviarMensaje(ChatGrupal("", mensajeEncrypted, CorreoUsuario, nombre, ServerValue.TIMESTAMP, false, ""), true)
                    }else{
                        enviarMensaje(ChatGrupal("", mensaje, CorreoUsuario, nombre, ServerValue.TIMESTAMP, false, ""), false)
                    }

                    txtEnviarMensaje.text.clear()
                }
            }

        }

        //Prueba cambio de color iconImage
        iconLock.setOnClickListener{
            if(entro == false){
                DrawableCompat.setTint(iconLock.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.white))
                entro = true
                encryptCondition = false
                Toast.makeText(this, "Encriptacion apagada", Toast.LENGTH_SHORT).show()
            }else{
                DrawableCompat.setTint(iconLock.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.green))
                Toast.makeText(this, "Encriptacion encendida", Toast.LENGTH_SHORT).show()
                entro = false
                encryptCondition = true
            }

        }

        //Prueba imagen chat iconLock
        iconImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"), 123)
        }

        if(condition == true){
            rvChatGrupal.adapter = adaptadorMensaje
            recibirMensajeSub()
        }else{
            rvChatGrupal.adapter = adaptadorMensaje
            recibirMensaje()
        }

        //Ejemplo de encriptacion
        //val textoCifrado = cifrar("Hola", "Fer1234")
        //val textoOriginal = descifrar(textoCifrado, "Fer1234")


    }

    //Prueba imagenes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123 && resultCode == android.app.Activity.RESULT_OK && data!!.data!=null){
            val loadingBar = ProgressDialog(this)
            loadingBar.setMessage("Enviando imagen..")
            loadingBar.show()
            val fileUri = data.data
            val storageRef = FirebaseStorage.getInstance().reference.child("imagenesChats")
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


                    if(condition == true) {
                        enviarMensajeSub(
                            ChatGrupal("", "", CorreoUsuario, nombre, ServerValue.TIMESTAMP, false, url), false
                        )
                    }else{
                        enviarMensaje(ChatGrupal("", "", CorreoUsuario, nombre, ServerValue.TIMESTAMP,  false, url), false)
                    }

                    Toast.makeText(this, "Imagen subida con exito", Toast.LENGTH_SHORT).show()
                    loadingBar.hide()
                }
            }
        }
    }


    private fun enviarMensaje(mensaje: ChatGrupal, cifrado: Boolean){
        //CARRERA USUARIO ES EL NOMBRE DEL GRUPO
        val child = chatRef.child(CarreraUsuario).push()
        mensaje.id = child.key ?:""
        mensaje.encrypt = cifrado
        child.setValue(mensaje)
    }
    private fun enviarMensajeSub(mensaje: ChatGrupal, cifrado: Boolean){
        //CARRERA USUARIO ES EL NOMBRE DEL GRUPO
        val child = chatRef.child(NombreGrupo).push()
        mensaje.id = child.key ?:""
        mensaje.encrypt = cifrado
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

                    if(mensaje.encrypt){
                        val mensajeDesencrypt = descifrar(mensaje.contenido, "FER12345")
                        mensaje.contenido = mensajeDesencrypt
                    }
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
                    if(mensaje.encrypt){
                        val mensajeDesencrypt = descifrar(mensaje.contenido, "FER12345")
                        mensaje.contenido = mensajeDesencrypt
                    }
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

    //AES
    private fun cifrar(textoPlano: String, llave: String): String{
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMS)

        val llaveBytesFinal = ByteArray(16)
        val llaveByteOriginal = llave.toByteArray(charset("UTF-8"))

        System.arraycopy(
            llaveByteOriginal,
            0,
            llaveBytesFinal,
            0,
            Math.min(
                llaveByteOriginal.size,
                llaveBytesFinal.size
            )
        )


        //La llave
        val secretKeySpec: SecretKeySpec = SecretKeySpec(
            llaveBytesFinal,
            "AES"
        )
        //VECTOR DE INICIALIZACION NOSR SIRVE PARA EL CIFRADO
        val vectorInit = IvParameterSpec(llaveBytesFinal)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, vectorInit)

        //texto que queremos cifrar
        val textoCifrado = cipher.doFinal(textoPlano.toByteArray(charset("UTF-8")))

        val resultadoEnBase = String (Base64.encode(textoCifrado, Base64.NO_PADDING))
        return resultadoEnBase
    }

    private fun descifrar(textoCifrado: String, llave: String): String{

        //Descifra del base 64
        val textoCifradoBytes = Base64.decode(textoCifrado, Base64.NO_PADDING)

        //Descifrar del AES

        val cipher = Cipher.getInstance(CIPHER_TRANSFORMS)

        val llaveBytesFinal = ByteArray(16)
        val llaveByteOriginal = llave.toByteArray(charset("UTF-8"))

        System.arraycopy(
            llaveByteOriginal,
            0,
            llaveBytesFinal,
            0,
            Math.min(
                llaveByteOriginal.size,
                llaveBytesFinal.size
            )
        )


        //La llave
        val secretKeySpec: SecretKeySpec = SecretKeySpec(
            llaveBytesFinal,
            "AES"
        )
        //Desciframos
        val vectorInit = IvParameterSpec(llaveBytesFinal)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, vectorInit)

        val textoPlano = String(cipher.doFinal(textoCifradoBytes))

        return textoPlano
    }

}