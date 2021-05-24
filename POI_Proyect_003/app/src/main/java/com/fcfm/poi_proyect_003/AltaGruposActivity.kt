package com.fcfm.poi_proyect_003

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Clases.SubGrupos
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.group_form.*
import kotlinx.android.synthetic.main.mensaje.*
import java.io.File
import java.net.URI


class AltaGruposActivity: AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val subRef = database.getReference("SubGrupos")
    private val Ref = database.getReference()

    private val storageRef = FirebaseStorage.getInstance().reference


    var carreraUser = ""
    var correoUser = ""
    var idSubgrupoCreado = ""
    var imagenPath = ""
    var imagenUrl = ""
    var nombreGrupo = ""

    //Variables para guardar los datos del usuario
    var nombreU: String = ""
    var carreraU: String = ""
    var correoU: String = ""
    var idU: String = ""
    var estadoU: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_form)

        //Se obtiene la carrera para saber a cual pertenece el subgrupo
        if(intent.hasExtra("carrera")){
            carreraUser = intent.getStringExtra("carrera").toString()
        }

        var user = Firebase.auth.currentUser
        user?.let {
            //Obtiene el correo del usuario para la busqueda
            var email = user.email
            if(email != null){
                correoUser = email
            }

        }

        //Busqueda del usuario en base al correo para obtener todos sus datos
        Ref.child("Usuarios").orderByChild("correo").equalTo(correoUser)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children){
                            val user = snap.getValue(Usuarios::class.java) as Usuarios
                            nombreU = user.nombre
                            carreraU = user.carrera
                            correoU = user.correo
                            idU = user.idAlumno
                            estadoU = user.estado

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })

        //Boton para cargar imagen
        val subirImagen: Button = findViewById(R.id.btnSubirImagenSG)
        subirImagen.setOnClickListener{
            seleccionarImagen(ImageProvider.GALLERY)
        }


        //Botton para agregar un grupo
        val nuevoGrupo: Button = findViewById(R.id.btnAgregarGrupo)
        nuevoGrupo.setOnClickListener{
            /*val*/ nombreGrupo = etNombreGrupo.text.toString()
            val descGrupo = etDescGrupo.text.toString()
            if(nombreGrupo != "") {
                //agregarGrupoBD(nombreGrupo, descGrupo)
                val file:File = File(imagenPath)
                subirImagen(file,nombreGrupo)
                ///*idSubgrupoCreado = */crearGrupo(SubGrupos("", carreraUser, nombreGrupo, imagenUrl), Usuarios(idU, nombreU, "", carreraU, correoU, estadoU))
                Toast.makeText(applicationContext, "Grupo creado con exito", Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Toast.makeText(
                        applicationContext,
                        "Ingresa un nombre para el grupo",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data
            findViewById<ImageView>(R.id.ivSubgrupo).setImageURI(fileUri)

            //You can get File object from intent
            //val file:File = ImagePicker.getFile(data)!!


            //You can also get File Path from intent
            //val filePath:String = ImagePicker.getFilePath(data)!!

            imagenPath = ImagePicker.getFilePath(data)!!



        } else if (resultCode == ImagePicker.RESULT_ERROR) {
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

        var urlImage:String = "0"

        //Mostramos el progressBar
        findViewById<ProgressBar>(R.id.progressBar3).visibility = android.view.View.VISIBLE

        val imagenesSubgruposRef = storageRef.child("imagenesSubgrupos/$idSubgrupo.SubgruposImagen") //Enviar id del subgrupo

        imagenesSubgruposRef.putFile(Uri.fromFile(archivoImagen))

                .addOnSuccessListener {
                    findViewById<ProgressBar>(R.id.progressBar3).visibility = android.view.View.GONE

                    imagenesSubgruposRef.downloadUrl.addOnSuccessListener {
                        Log.w("LIGA",it.toString())
                        imagenUrl = it.toString()
                        crearGrupo(SubGrupos("", carreraUser, nombreGrupo, imagenUrl), Usuarios(idU, nombreU, "", carreraU, correoU, estadoU))
                        //obtenerURL(it.toString())


                    }

                    /*imagenesSubgruposRef.downloadUrl.addOnCompleteListener{
                        task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            imagenUrl = "12345"//downloadUri.toString()
                        }
                    }*/

                }

                .addOnFailureListener{
                    findViewById<ProgressBar>(R.id.progressBar3).visibility = android.view.View.GONE
                }
        //return urlImage
    }

    /*fun agregarGrupoB/D(nombreGrupo:String,descGrupo:String){

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        var reference:DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var grupo:HashMap<String, String> = HashMap()
        grupo.put("nombreGrupo", nombreGrupo)
        grupo.put("descGrupo", descGrupo)
        grupo.put("idCreador", firebaseUser!!.uid)

        reference!!.child("Grupos").push().setValue(grupo)

        finish()

        /*var database: DatabaseReference
        database = FirebaseDatabase.getInstance().reference

        var firebaseUser: FirebaseUser? = null
        var reference: DatabaseReference? = null
        var userId = intent.getStringExtra("idAlmuno")
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId!!)
        database.child("Grupos").setValue(Grupo(nombreGrupo,descGrupo,userId.toString()))*/

    }*/

    private fun crearGrupo(subGrupos: SubGrupos, usuarios: Usuarios){//: String{
        val mensajeFirebase = subRef.push()
        subGrupos.id = mensajeFirebase.key ?: "" //Puede ser nulo






        //Mostramos el progressBar
        /*findViewById<ProgressBar>(R.id.progressBar3).visibility = android.view.View.VISIBLE
        val archivoImagen:File = File(imagenPath)

        val imagenesSubgruposRef = storageRef.child("imagenesSubgrupos/${subGrupos.id}") //Enviar id del subgrupo

        imagenesSubgruposRef.putFile(Uri.fromFile(archivoImagen))

            .addOnSuccessListener {
                findViewById<ProgressBar>(R.id.progressBar3).visibility = android.view.View.GONE

                imagenesSubgruposRef.downloadUrl.addOnSuccessListener {


                }

            }

            .addOnFailureListener{
                findViewById<ProgressBar>(R.id.progressBar3).visibility = android.view.View.GONE
            }

         */





        /*val ImagenUrl: String = "Prueba"
        subGrupos.imagen = ImagenUrl.toString()//"Hola"//it.toString()*/

        mensajeFirebase.setValue(subGrupos)
        val child = subRef.child(subGrupos.id).
        child("Participantes").push()

        child.setValue(usuarios)

        //return subGrupos.id

    }

    /*private fun obtenerURL(urlImage: String){
        imagenUrl = urlImage
    }*/
}