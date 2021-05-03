package com.fcfm.poi_proyect_003

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.fcfm.poi_proyect_003.Fragment.FragmentoChat
import com.fcfm.poi_proyect_003.Fragment.FragmentoGrupo
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity() : AppCompatActivity(){
    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference()
    private lateinit var auth: FirebaseAuth

    var correo  = ""
    var nombre = ""
    var carrera = ""
    var i = "0"
    var id = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        title = "FORO"
        var user = Firebase.auth.currentUser
        user?.let {
            val name = user.displayName
            val email = user.email
            val uid = user.uid
            if(email != null){
                correo = email
            }
        }
        auth = FirebaseAuth.getInstance()

        chatRef.child("Usuarios").orderByChild("correo").equalTo(correo).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children){

                    val key = snap.key.toString()
                    if(i == "0"){
                        chatRef.child("Usuarios").child(key).child("estado").setValue("online")
                        i = "1"
                    }
                    val usuario = snap.getValue(Usuarios::class.java) as Usuarios
                    carrera = usuario.carrera
                    nombre = usuario.nombre
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position){
                    0 -> changeFragment(FragmentoGrupo(), "fragmentoGrupo")
                    1 -> {
                        changeFragment(FragmentoChat(), "fragmentoChat")
                    }
                    else -> changeFragment(FragmentoChat(), "FragmentoChat")
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }


        })

        tabLayout.selectTab(tabLayout.getTabAt(0))
        changeFragment(FragmentoChat(), "fragmentoChat")

        btnChatGrupal.setOnClickListener {

            val intentChatG = Intent(this, ChatGrupoActivity::class.java)
            intent.putExtra("Correo01", user.email)
            this.startActivity(intentChatG)
            //startActivity(Intent(this, ChatGrupoActivity::class.java))

        }
        btnContactos.setOnClickListener {
            startActivity(Intent(this, UsuariosActivity::class.java))

        }


    }
    @JvmName("getCarrera1")
    fun getCarrera(): String{
        return carrera
    }

    @JvmName("getCorreo1")
    fun getCorreo(): String {
        return correo
    }

    @JvmName("getId1")
    fun getId(): String {
        return id
    }

    private fun changeFragment(fragment: Fragment, tag: String){
        val currentFragment = supportFragmentManager.findFragmentByTag(tag)

        //Si no existe
        if(currentFragment == null || currentFragment.isVisible.not()){
            //Se cambia el fragmento
            supportFragmentManager.beginTransaction().replace(R.id.framentContainer, fragment, tag)
                    .commit()
        }
    }
}