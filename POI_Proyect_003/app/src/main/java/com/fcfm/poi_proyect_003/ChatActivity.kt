package com.fcfm.poi_proyect_003

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fcfm.poi_proyect_003.Adaptadores.ChatIndividualAdapter
import com.fcfm.poi_proyect_003.Adaptadores.UsuariosAdapter
import com.fcfm.poi_proyect_003.Clases.Usuarios
import com.fcfm.poi_proyect_003.Clases.chatIndividual
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat2.*
import kotlinx.android.synthetic.main.activity_chat2.imgBack
import kotlinx.android.synthetic.main.activity_usuarios.*
import java.lang.ref.Reference

class ChatActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    var reference:DatabaseReference? = null
    var chatList = ArrayList<chatIndividual>()
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat2)
        chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        var intent = getIntent()
        var userName = intent.getStringExtra("userName")
        var userId = intent.getStringExtra("idAlmuno")



        imgBack.setOnClickListener{
            onBackPressed()
        }
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId!!)

        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
               val user = snapshot.getValue(Usuarios::class.java)
                tvUserName.text = user!!.nombre
            }

        })

        btnSendMessage.setOnClickListener {
            var message:String = etMessage.text.toString()
            if(message.isEmpty()){
                Toast.makeText(applicationContext,"Mensaje Vacío", Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            }else{
                sendMessage(firebaseUser!!.uid, userId, message)
                etMessage.setText("")
            }
        }

            readMessage(firebaseUser!!.uid, userId)

    }

    private fun sendMessage(senderId:String, receiverId: String, message: String){
        var reference:DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap:HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("chatInd").push().setValue(hashMap)
    }
    fun readMessage(senderId: String, receiverId: String){
        val databaseReference:DatabaseReference =
            FirebaseDatabase.getInstance().getReference("chatInd")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val chat = dataSnapShot.getValue(chatIndividual::class.java)
                    if(chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)){
                        chatList.add(chat)
                    }
                }
                val chatAdapter = ChatIndividualAdapter(this@ChatActivity, chatList)
                chatRecyclerView.adapter = chatAdapter
            }
        })

    }
}