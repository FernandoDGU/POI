package com.example.myapplication

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class HolderMensaje(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var nombre: TextView
    var mensaje: TextView
    var hora: TextView
    var fotoMensajePerfil: CircleImageView
    var fotoMensaje: ImageView

    init {
        nombre = itemView.findViewById<View>(R.id.nombreMensaje) as TextView
        mensaje = itemView.findViewById<View>(R.id.mensajeMensaje) as TextView
        hora = itemView.findViewById<View>(R.id.horaMensaje) as TextView
        fotoMensajePerfil = itemView.findViewById<View>(R.id.fotoPerfilMensaje) as CircleImageView
        fotoMensaje= itemView.findViewById<View>(R.id.mensajeFoto) as ImageView

    }



}