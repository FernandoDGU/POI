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
import com.fcfm.poi_proyect_003.Clases.chatIndividual
import com.fcfm.poi_proyect_003.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChatIndividualAdapter(private val context: Context, private val chatList: ArrayList<chatIndividual>) :
    RecyclerView.Adapter<ChatIndividualAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_der_mensaje, parent, false)
                return ViewHolder(view)
        }else{
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_izq_mensaje, parent, false)
            return ViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
       return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.txtUserName.text = chat.message
    }
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtUserName: TextView = view.findViewById(R.id.tvMessage)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if(chatList[position].senderId == firebaseUser!!.uid){
            MESSAGE_TYPE_RIGHT
        }else{
            MESSAGE_TYPE_LEFT
        }
    }
}