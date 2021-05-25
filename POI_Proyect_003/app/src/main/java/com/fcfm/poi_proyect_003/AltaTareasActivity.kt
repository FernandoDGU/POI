package com.fcfm.poi_proyect_003

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Clases.Tareas
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.mensaje.*
import kotlinx.android.synthetic.main.tareas_form.*
import java.io.File

class AltaTareasActivity: AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private var tareaRef = database.getReference("Tareas")
    private lateinit var tarea: Tareas
    private val storageRef = FirebaseStorage.getInstance().reference

    var carreraUser: String = ""
    var verTarea: String = ""

    var imagenPath = ""
    var imagenUrl = ""

    var titulo = ""
    var desc = ""
    var fin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tareas_form)


        //Obtener la carrera
        if(intent.hasExtra("Carrera")){
            carreraUser = intent.getStringExtra("Carrera").toString()
        }

        if(intent.hasExtra("Ver")){
            verTarea = intent.getStringExtra("Ver").toString()
        }

        if(verTarea == "Ver"){

            tarea = Tareas(
                intent.getStringExtra("id")!!,
                intent.getStringExtra("UserId")!!,
                intent.getStringExtra("Titulo")!!,
                intent.getStringExtra("Desc")!!,
                intent.getBooleanExtra("Fin", false),
                intent.getStringExtra("IdGrupo")!!
            )
            txtTituloTarea.setText(tarea.Titulo)
            txtDescripcionTarea.setText(tarea.Desc)

            btnAddTarea.visibility = android.view.View.GONE
            btnImagenTarea.visibility = android.view.View.GONE
            txtTituloTarea.isEnabled = false
            txtDescripcionTarea.isEnabled = false
        }

        btnAddTarea.setOnClickListener{
            titulo = txtTituloTarea.text.toString()
            desc = txtDescripcionTarea.text.toString()
            fin = false
            val file:File = File(imagenPath)
            subirImagen(file,titulo)
            //agregarTarea(Tareas("", FirebaseAuth.getInstance().currentUser?.uid.toString(),
            //titulo, desc, fin, carreraUser))
            finish()
        }

        btnImagenTarea.setOnClickListener{
                seleccionarImagen(ImageProvider.GALLERY)
        }

    }

    //Subir imagenes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val fileUri = data?.data
            findViewById<ImageView>(R.id.ivSubirTarea).setImageURI(fileUri)
            imagenPath = ImagePicker.getFilePath(data)!!
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun seleccionarImagen(provider: ImageProvider){

        val intentGallery = ImagePicker.with(this)
            .crop()        //Crop image(Optional), Check Customization for more option
            .cropOval()    //Allow dimmed layer to have a circle inside
            .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080(Optional)
            .provider(provider)
            .createIntent()

        startActivityForResult(intentGallery,1)
    }

    private fun subirImagen(archivoImagen: File, idSubgrupo: String){//:String{

        //Mostramos el progressBar
        findViewById<ProgressBar>(R.id.progressBar4).visibility = android.view.View.VISIBLE
        val tsLong = System.currentTimeMillis()
        val ts = tsLong.toString()

        val imagenesSubgruposRef = storageRef.child("imagenesTareas/$idSubgrupo.TareasImagen$ts") //Enviar id del subgrupo

        imagenesSubgruposRef.putFile(Uri.fromFile(archivoImagen))

            .addOnSuccessListener {
                findViewById<ProgressBar>(R.id.progressBar4).visibility = android.view.View.GONE

                imagenesSubgruposRef.downloadUrl.addOnSuccessListener {
                    //Log.w("LIGA",it.toString())
                    imagenUrl = it.toString()
                    //MandarDatos(userName, password, email, carreraUsuario, estado, imagenUrl)
                    agregarTarea(Tareas("", FirebaseAuth.getInstance().currentUser?.uid.toString(),
                       titulo, desc, fin, carreraUser, imagenUrl))
                }

            }

            .addOnFailureListener{
                findViewById<ProgressBar>(R.id.progressBar4).visibility = android.view.View.GONE
            }
    }


    private fun agregarTarea(ptarea: Tareas){
        val work = tareaRef.push()
        ptarea.id = tareaRef.key ?: ""
        work.setValue(ptarea)
    }





}