package com.fcfm.poi_proyect_003.Adaptadores

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.fcfm.poi_proyect_003.ChatActivity
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.fcfm.poi_proyect_003.Fragment.FragmentoChat
import com.fcfm.poi_proyect_003.R
import com.fcfm.poi_proyect_003.UsuariosActivity
import com.google.android.material.internal.ContextUtils.getActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat_grupal.*

interface ContactoClick {
    fun detalle(Usuario:Usuarios)
}

class UsuariosAdapter(private val context: FragmentoChat, private val userList:ArrayList<Usuarios>,
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
        if(user.estado == "Conectado"){
            holder.txtEstado.setTextColor(Color.parseColor("#acff7a"));
            //DrawableCompat.setTint(iconLock.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.green))
        }else{
            holder.txtEstado.setTextColor(Color.parseColor("#ff7a7a"));
        }

        holder.txtEstado.text = user.estado
        holder.Foto
        val imagenUser = holder.Foto
        val imageUri =  Uri.parse(user.imagen)
        Picasso.get().load(imageUri).into(imagenUser)
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
        val Foto: ImageView = view.findViewById(R.id.ivChatUsuario)
    }
}