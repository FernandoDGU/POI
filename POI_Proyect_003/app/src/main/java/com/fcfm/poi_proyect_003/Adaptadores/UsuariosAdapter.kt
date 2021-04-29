package com.fcfm.poi_proyect_003.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi_proyect_003.ChatActivity
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.fcfm.poi_proyect_003.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UsuariosAdapter(private val context: Context, private val userList:ArrayList<Usuarios>) :
    RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
       // val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        holder.txtUserName.text = user.nombre
       // holder.txtTemp.text = user.estado
        holder.layoutUser.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userName", user.nombre)
            intent.putExtra("idAlmuno", user.idAlumno)
            context.startActivity(intent)
        }
    }
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtUserName: TextView = view.findViewById(R.id.userName)
        val txtTemp:TextView = view.findViewById(R.id.temp)
        val layoutUser:LinearLayout = view.findViewById(R.id.layoutUser)
    }
}