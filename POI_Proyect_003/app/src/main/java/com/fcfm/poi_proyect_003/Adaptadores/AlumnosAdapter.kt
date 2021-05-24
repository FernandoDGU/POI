package com.fcfm.poi_proyect_003.Adaptadores

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi_proyect_003.AgregarSubActivity
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_usuario2.view.*
import java.security.AccessControlContext


interface UsuarioListener {
    fun usuarioListener(usuarios: Usuarios)
}

class AlumnosAdapter(private val context: AgregarSubActivity,
                     private val usuariosList:MutableList<Usuarios>,
                     private val usuarioListener:UsuarioListener):
    RecyclerView.Adapter<AlumnosAdapter.AddUserViewHolder>() {

        //Constructor
        inner class AddUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun binData(user : Usuarios){
                itemView.txtNombreAdd.text = user.nombre
                itemView.txtCorreoAdd.text = user.correo

                val imagen = itemView.ivAddUserSub
                val imageUri = Uri.parse(user.imagen)
                Picasso.get().load(imageUri).into(imagen)
                //Click y se mandan los datos
                itemView.cAddUser.setOnClickListener(null)
                itemView.cAddUser.setOnClickListener{
                    usuarioListener.usuarioListener(user)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddUserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.fcfm.poi_proyect_003.R.layout.item_usuario2, parent, false)
        return AddUserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AddUserViewHolder, position: Int) {
        holder.binData(usuariosList[position])
    }

    override fun getItemCount(): Int = usuariosList.size


}