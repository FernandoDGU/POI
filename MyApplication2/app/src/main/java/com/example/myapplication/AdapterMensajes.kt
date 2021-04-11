package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Entidades.Mensaje
import java.util.*

class AdapterMensajes(private val c: Context) : RecyclerView.Adapter<HolderMensaje>() {
    private val listMensaje: MutableList<Mensaje> = ArrayList()
    fun addMensaje(m: Mensaje) {
        listMensaje.add(m)
        notifyItemInserted(listMensaje.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMensaje {
        val v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes, parent, false)
        return HolderMensaje(v)
    }

    override fun onBindViewHolder(holder: HolderMensaje, position: Int) {
        holder.nombre.text = listMensaje[position].nombre
        holder.mensaje.text = listMensaje[position].mensaje
        holder.hora.text = listMensaje[position].hora.toString()
        if(listMensaje[position].type_mensaje.equals("2")){
            holder.fotoMensaje.visibility = View.VISIBLE
            holder.mensaje.visibility = View.VISIBLE
            Glide.with(c).load(listMensaje[position].urlFoto).into(holder.fotoMensaje)
        }else if (listMensaje[position].type_mensaje.equals("1")) {
            holder.fotoMensaje.visibility = View.GONE
            holder.mensaje.visibility = View.VISIBLE
        }

       // var codigoHora: Long? = listMensaje[position].hora
       // var d = codigoHora?.let { Date(it) }
       // val sdf = SimpleDateFormat("hh:mm:ss a") //a pm o am
       // holder.hora.text = codigoHora.toString()
    }

    override fun getItemCount(): Int {
        return listMensaje.size
    }

}