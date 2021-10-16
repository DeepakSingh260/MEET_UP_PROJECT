package com.example.meet_up_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class Home : Fragment() {

    private var _db = FirebaseDatabase.getInstance().getReference("chats")
    private lateinit var recyclerView: RecyclerView
    private lateinit var profileList: MutableList<profile_data>
    private lateinit var adapter:profileView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        profileList = ArrayList()
        recyclerView = requireView().findViewById(R.id.messenger_recycle_view)
        adapter = profileView(requireContext() , profileList as ArrayList<profile_data>)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        _db.child(Firebase.auth.currentUser!!.uid+"/").addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                profileList.clear()

                for(post in snapshot.children){
                    val id = post.key.toString()
                    Log.d(TAG , post.value.toString())

                    var name:String?  = null
                    var pic:String? = null
                    for(shot in post.children.iterator()) {
//                        Toast.makeText(requireContext() , shot.toString() , Toast.LENGTH_LONG).show()
                        Log.d(TAG,shot.toString())
                         name = shot.child("name").value.toString()
                        pic = shot.child("photoUrl").value.toString()
                    }
                    profileList.add(profile_data(id,name , pic))
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    companion object {
        const val TAG = "HOME"
    }
}

class profileView(requireContext:Context , profileList : ArrayList<profile_data>):RecyclerView.Adapter<profileView.profileViewHolder>(){

    val ctx = requireContext
    val profileList = profileList

    class profileViewHolder(view:View) : RecyclerView.ViewHolder(view){

        val name:TextView
        val pic : ImageView
        val btn:Button
        init {
            name = view.findViewById(R.id.messager_name)
            pic = view.findViewById(R.id.messenger_image)
            btn = view.findViewById(R.id.messenger_btn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): profileViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_layout,parent,false)
        return profileViewHolder(view)
    }

    override fun onBindViewHolder(holder: profileViewHolder, position: Int) {

        holder.name.text= profileList.get(position).name
        Picasso.get().load(profileList.get(position).profilePic).into(holder.pic)
        val id = profileList.get(position).id
        val name = "ID"
        holder.btn.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                v!!.context.startActivity(Intent(ctx , Message::class.java).apply {
                    Toast.makeText(ctx , id , Toast.LENGTH_SHORT).show()
                    putExtra(name,id)
                })
            }
        })

    }

    override fun getItemCount(): Int {
        return profileList.size
    }
}

data class profile_data(
    val id:String,
    val name:String?,
    val profilePic:String?
)