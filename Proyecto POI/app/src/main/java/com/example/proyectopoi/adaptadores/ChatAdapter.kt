package com.example.proyectopoi.adaptadores

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopoi.R
import com.example.proyectopoi.modelos.Mensaje
import kotlinx.android.synthetic.main.custom_item_mensaje.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(private val listaMensajes: MutableList<Mensaje>) :
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun asignarInformacion(mensaje: Mensaje) {

            itemView.tvUsuario.text = mensaje.de
            itemView.tvMensaje.text = mensaje.contenido

            val dateFormater = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
            val fecha = dateFormater.format(Date(mensaje.timeStamp as Long))

            itemView.tvFecha.text = fecha

            val params = itemView.contenedorMensaje.layoutParams

            if (mensaje.esMio) {

                val newParams = FrameLayout.LayoutParams(
                        params.width,
                        params.height,
                        Gravity.END
                )
                itemView.contenedorMensaje.layoutParams = newParams

            } else {

                val newParams = FrameLayout.LayoutParams(
                        params.width,
                        params.height,
                        Gravity.START
                )
                itemView.contenedorMensaje.layoutParams = newParams
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        return ChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.custom_item_mensaje, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        holder.asignarInformacion(listaMensajes[position])
    }

    override fun getItemCount(): Int = listaMensajes.size
}