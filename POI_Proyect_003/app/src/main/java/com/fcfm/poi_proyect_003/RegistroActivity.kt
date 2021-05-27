/*package com.fcfm.poi_proyect_003

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    var carrera: String = "LMAD"
    private lateinit var auth: FirebaseAuth //Para autentificacion por correo
    private var database = FirebaseDatabase.getInstance()
    private var userRef = database.getReference("Usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        title = "Registrar Usuario"
        auth = FirebaseAuth.getInstance()

        btnAceptar.setOnClickListener {
            val nombreUsuario = txtRegistroNombre.text.toString()
            val passwordUsuario = txtRegistroPass.text.toString()
            val carreraUsuario = carrera
            val correoUsuario = txtRegistroCorreo.text.toString()
            val estado = "Desconectado"

            MandarDatos(nombreUsuario, passwordUsuario, carreraUsuario, correoUsuario, estado)
        }

    }   private fun MandarDatos( nombreUsuario:String, PasswordUsuario:String, carreraUsuario:String, correoUsuario:String, estado:String) {
        auth.createUserWithEmailAndPassword(nombreUsuario, PasswordUsuario)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userC: FirebaseUser? = auth.currentUser
                        val userId:String = userC!!.uid
                        var nombre = nombreUsuario
                        var profileupdate = UserProfileChangeRequest.Builder().setDisplayName(nombre).build()
                        FirebaseAuth.getInstance().currentUser?.updateProfile(profileupdate)
                                ?.addOnCompleteListener(this){task2 -> if(!task2.isSuccessful){
                                    Toast.makeText(this, "no se pudo poner nombre", Toast.LENGTH_SHORT).show()
                                }else{
                                    var usuariodb = userRef.push()
                                    var usuario = Usuarios(
                                            userId,
                                            nombreUsuario,
                                            PasswordUsuario,
                                            carreraUsuario,
                                            correoUsuario,
                                            estado
                                    )
                                    usuariodb.setValue(usuario)
                                    Toast.makeText(this, "usuario ingresado correctamente", Toast.LENGTH_SHORT).show()
                                    //startActivity(Intent(this, MainActivity::class.java))
                                }
                                }
                    } else {
                        Toast.makeText(this, "no se pudo guardar el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}*/

package com.fcfm.poi_proyect_003


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.mensaje.view.*
import java.io.File

class  RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val storageRef = FirebaseStorage.getInstance().reference

    var carrera: String = ""
    var imagenPath = ""
    var imagenUrl = ""

    var estado = ""
    var userName = ""
    var email = ""
    var password = ""
    var carreraUsuario = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()

        var spinnerRef = findViewById(R.id.carreraRegistro) as Spinner
        val carreras = arrayOf("LMAD", "LCC", "LA", "LF", "LM", "LSTI")

        spinnerRef.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, carreras)

        spinnerRef.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                carrera = carreras.get(0)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                carrera = carreras.get(position)
            }

        }

        //Agregar un usuario
        btnAceptar.setOnClickListener {
            userName = txtRegistroNombre.text.toString()
            email = txtRegistroCorreo.text.toString()
            password = txtRegistroPass.text.toString()
            carreraUsuario = carrera
            estado = "Desconectado"
            MandarDatos(userName, password, email, carreraUsuario, estado,
                "https://firebasestorage.googleapis." +
                        "com/v0/b/proyectopoi003.appspot.com/o/imagenesUsuarios%2F-" +
                        "Mag71C2ihH1IcIHgdkq.jgp?alt=media&token=a94541bd-964e-41b3-894a-03daa3df1681")
            //val file:File = File(imagenPath)
            //subirImagen(file,email)
            Toast.makeText(applicationContext, "Usuario creado con exito", Toast.LENGTH_SHORT).show()

        }

        //Boton para subir imagen
        btnSubirImagenRegistro.setOnClickListener{
            //seleccionarImagen(ImageProvider.GALLERY)
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"), 123)
        }

    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val fileUri = data?.data
            findViewById<ImageView>(R.id.ivRegistro).setImageURI(fileUri)
            imagenPath = ImagePicker.getFilePath(data)!!
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }*/

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
                    val imagen = ivRegistro
                    val imageUri = Uri.parse(url)
                    Picasso.get().load(imageUri).into(imagen)
                    Toast.makeText(this, "Imagen subida con exito", Toast.LENGTH_SHORT).show()
                    loadingBar.hide()
                }
            }
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
        findViewById<ProgressBar>(R.id.progressBar2).visibility = android.view.View.VISIBLE

        val imagenesSubgruposRef = storageRef.child("imagenesUsuarios/$idSubgrupo.UsuariosImagen") //Enviar id del subgrupo

        imagenesSubgruposRef.putFile(Uri.fromFile(archivoImagen))

            .addOnSuccessListener {
                findViewById<ProgressBar>(R.id.progressBar2).visibility = android.view.View.GONE

                imagenesSubgruposRef.downloadUrl.addOnSuccessListener {
                    //Log.w("LIGA",it.toString())
                    imagenUrl = it.toString()
                    MandarDatos(userName, password, email, carreraUsuario, estado, imagenUrl)
                }

            }

            .addOnFailureListener{
                findViewById<ProgressBar>(R.id.progressBar2).visibility = android.view.View.GONE
            }
    }

    private fun MandarDatos(userName:String,password:String, email:String, carreraUsuario:String, estado:String, imagen:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val user: FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("idAlumno",userId)
                    hashMap.put("nombre",userName)
                    hashMap.put("contrasena",password)
                    hashMap.put("carrera",carreraUsuario)
                    hashMap.put("estado",estado)
                    hashMap.put("correo",email)
                    hashMap.put("imagen",imagen)

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if (it.isSuccessful){
                            //open home activity
                            txtRegistroNombre.setText("")
                            txtRegistroCorreo.setText("")
                            txtRegistroPass.setText("")
                            //REDIRECCIONA A OTRA PANTALLA
                            val intent = Intent(this@RegistroActivity,
                                MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }
}