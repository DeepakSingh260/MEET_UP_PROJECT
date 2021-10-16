package com.example.meet_up_project

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Message : AppCompatActivity() {

    private val _db = FirebaseDatabase.getInstance().getReference("chats/")
    private lateinit var sent_btn:Button
    private lateinit var editMessage:EditText
    private lateinit var image:ImageView
    private lateinit var profile_name:TextView
    private val user = Firebase.auth.currentUser
    private lateinit var chatList: MutableList<ChatInfo>
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var adapter:messageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        sent_btn = findViewById(R.id.sent_btn)
        editMessage  = findViewById(R.id.type_message)
        chatRecyclerView = findViewById(R.id.recycleChatMessage)
        image = findViewById(R.id.profile_image)
        profile_name  = findViewById(R.id.profile_name_txt)
        chatList = ArrayList()

        adapter = messageAdapter(applicationContext , chatList as ArrayList<ChatInfo>)
        chatRecyclerView.adapter = adapter
        chatRecyclerView.layoutManager  = LinearLayoutManager(applicationContext)

        val userId = intent.extras?.getString("ID")

        _db.child(user!!.uid+"/").child(userId.toString()).addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()

                for (postSnapShot in snapshot.children.iterator()){
                    val id = postSnapShot.child("id").value
                    val timeStamp = postSnapShot.child("timeStamp").value
                    val photoUrl = postSnapShot.child("photoUrl").value
                    val message = postSnapShot.child("message").value
                    val name = postSnapShot.child("name").value

                    chatList.add(ChatInfo(id.toString() , timeStamp.toString() , message.toString() ,photoUrl.toString(),name.toString()))
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        FirebaseDatabase.getInstance().getReference("profiles").child(userId.toString()+"/").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    profile_name.text = snapshot.child("name").value.toString()
                    Picasso.get().load(snapshot.child("profileUrl").value.toString()).into(image)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        sent_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val id = userId.toString()
                val message = editMessage.text.toString()
                val photoUrl = Firebase.auth.currentUser!!.photoUrl.toString()
                Toast.makeText(applicationContext ,  Firebase.auth.currentUser!!.photoUrl.toString(),Toast.LENGTH_LONG).show()
                val name = Firebase.auth.currentUser!!.displayName
                val timeStamp = SimpleDateFormat("HH:mm").format(Date())
                editMessage.text.clear()

                val push = _db.child(user!!.uid+"/").child(userId.toString()+"/").push()
                push.child("id").setValue(id)
                push.child("photoUrl").setValue(photoUrl)
                push.child("name").setValue(name)
                push.child("message").setValue(message)
                push.child("timeStamp").setValue(timeStamp)

                val pushto = _db.child(userId.toString()+"/").child(user!!.uid+"/").push()
                pushto.child("id").setValue(id)
                pushto.child("photoUrl").setValue(photoUrl)
                pushto.child("name").setValue(name)
                pushto.child("message").setValue(message)
                pushto.child("timeStamp").setValue(timeStamp)
            }

        })


    }
}

class messageAdapter(applicationContext : Context , chatList:ArrayList<ChatInfo>):RecyclerView.Adapter<messageAdapter.messageViewHolder>(){
    val chatList = chatList
    val ctx = applicationContext
    val user = Firebase.auth.currentUser!!.uid


    class messageViewHolder(view:View):RecyclerView.ViewHolder(view){
        val message:TextView
        val name : TextView
        val profilePhoto:ImageView
        val timeStamp:TextView

        init{
            message = view.findViewById(R.id.chat_message)
            name = view.findViewById(R.id.chat_profile_name)
            profilePhoto = view.findViewById(R.id.chat_image)
            timeStamp = view.findViewById(R.id.chat_time)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): messageViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_view_holder , parent,false)
        return messageViewHolder(view)
    }

    override fun onBindViewHolder(holder: messageViewHolder, position: Int) {

        holder.message.text = chatList.get(position).message
        holder.name.text = chatList.get(position).name
        holder.timeStamp.text = chatList.get(position).timeStamp
        Picasso.get().load(chatList.get(position).photoUrl).into(holder.profilePhoto)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}


data class ChatInfo(
    val id:String,
    val timeStamp:String,
    val message: String,
    val photoUrl:String,
    val name:String
)
