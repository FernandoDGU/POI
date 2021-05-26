package com.fcfm.poi_proyect_003.Adaptadores

import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi_proyect_003.Clases.ChatGrupal
import com.fcfm.poi_proyect_003.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_tareas.view.*
import kotlinx.android.synthetic.main.mensaje.view.*
import java.text.SimpleDateFormat
import java.util.*

class chatGrupalAdapter(private val listaMensajes : MutableList<ChatGrupal>):
    RecyclerView.Adapter<chatGrupalAdapter.chatGrupalViewHolder>() {

    class chatGrupalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun asignarInformacion(mensaje: ChatGrupal){
            //Pone los datos en la caja de mensajes
            itemView.tvUsuario.text = mensaje.deNombre
            itemView.tvMensaje.text = mensaje.contenido

            if(mensaje.image != ""){
                itemView.ivMessage.visibility = android.view.View.VISIBLE
                itemView.tvMensaje.visibility = android.view.View.GONE
                val imagen = itemView.ivMessage
                val imageUri = Uri.parse(mensaje.image)
                Picasso.get().load(imageUri).into(imagen)
            }else{
                itemView.ivMessage.visibility = android.view.View.GONE
                itemView.tvMensaje.visibility = android.view.View.VISIBLE
            }

            val fechaFormato = SimpleDateFormat("dd/MM/yyyy - HH::mm:ss", Locale.getDefault())
            val fechaconFormato = fechaFormato.format(Date(mensaje.timestamp as Long))
            itemView.tvFecha.text = fechaconFormato

            val parametros = itemView.contMensaje.layoutParams
            if(mensaje.esMio){
                val newParams = FrameLayout.LayoutParams(
                        parametros.width,
                        parametros.height,
                        Gravity.END
                )
                itemView.contMensaje.layoutParams = newParams
            }else{
                val newParams = FrameLayout.LayoutParams(
                        parametros.width,
                        parametros.height,
                        Gravity.START
                )
                itemView.contMensaje.layoutParams = newParams
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatGrupalViewHolder {
        return chatGrupalViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.mensaje, parent, false)
        )
    }

    override fun onBindViewHolder(holder: chatGrupalViewHolder, position: Int) {
        holder.asignarInformacion(listaMensajes[position])
    }
    override fun getItemCount(): Int = listaMensajes.size
}