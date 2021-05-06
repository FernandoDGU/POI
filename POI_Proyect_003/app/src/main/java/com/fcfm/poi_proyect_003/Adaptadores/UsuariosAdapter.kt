package com.fcfm.poi_proyect_003.Adaptadores

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi_proyect_003.ChatActivity
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.fcfm.poi_proyect_003.Fragment.FragmentoChat
import com.fcfm.poi_proyect_003.R
import com.fcfm.poi_proyect_003.UsuariosActivity
import com.google.android.material.internal.ContextUtils.getActivity

interface ContactoClick {
    fun detalle(Usuario:Usuarios)
}

class UsuariosAdapter(private val context: UsuariosActivity, private val userList:ArrayList<Usuarios>,
                      private val contactoClick: ContactoClick) :
    RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
       // val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        holder.txtUserName.text = user.nombre
        holder.txtEstado.text = user.estado
       //holder.txtTemp.text = user.estado
        holder.layoutUser.setOnClickListener {
            contactoClick.detalle(user)
            //val intent = Intent(context, ChatActivity::class.java)
            //intent.putExtra("userName", user.nombre)
            //intent.putExtra("idAlmuno", user.idAlumno)
            //context.startActivity(intent)
        }
    }
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtUserName: TextView = view.findViewById(R.id.userName)
        val txtEstado:TextView = view.findViewById(R.id.estado)
        val layoutUser:LinearLayout = view.findViewById(R.id.layoutUser)
    }
}