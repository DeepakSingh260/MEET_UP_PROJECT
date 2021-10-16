package com.example.meet_up_project

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class friends : Fragment() {
    private val _db = FirebaseDatabase.getInstance().getReference("profiles/")
    private lateinit var profileList: MutableList<profileInfo>
    private lateinit var recycleView:RecyclerView
    private lateinit var adapter: profileAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends,container,false)
    }

    override fun onStart() {
        super.onStart()
        profileList = ArrayList<profileInfo>()
        recycleView = requireView().findViewById(R.id.friendsRecyclerView)
        adapter = profileAdapter(profileList , requireContext())
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = adapter

        var tags:String? = null

        FirebaseDatabase.getInstance().getReference("profiles"+"/"+Firebase.auth.currentUser!!.uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tags = snapshot.child("tag").value.toString()
                    Toast.makeText(requireContext() , tags , Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        _db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d(TAG , snapshot.toString())
                for (postSnapShot in snapshot.children){
                    val userID = postSnapShot.key.toString()

                    profileList.removeAll(Collections.emptyList())
                    val photoUrl  = postSnapShot.child("profileUrl").value.toString()
                    val name = postSnapShot.child("name").value.toString()
                    val tag = postSnapShot.child("tag").value.toString()
                    Toast.makeText(requireContext() , "Khali "+tag , Toast.LENGTH_SHORT).show()
                    if (userID == Firebase.auth.currentUser!!.uid ){
                        continue
                    }else {
                        profileList.add(profileInfo(name, photoUrl, userID , tag))
                    }
                }
                adapter.notifyDataSetChanged()
                Log.d(TAG , "DATA SET Changes"+profileList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    companion object {

    }
}

class profileAdapter(profileList:List<profileInfo> , requireContext: Context): RecyclerView.Adapter<profileAdapter.profileViewHolder>(){
    var profileList = profileList
    var requireContext = requireContext
    class  profileViewHolder(view: View):RecyclerView.ViewHolder(view){
        var nameText:TextView
        var profilePic:ImageView
        var txt_btn: Button
        var txt_id:TextView
        val name = "ID"
        var tagTxt:TextView
        init{
            txt_id = view.findViewById(R.id.profiles_id)
            txt_btn = view.findViewById(R.id.message_btn)
            nameText = view.findViewById(R.id.other_profile_name)
            profilePic = view.findViewById(R.id.other_profile_image)
            tagTxt = view.findViewById(R.id.other_profile_tag)

            txt_btn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    view.context.startActivity(Intent(view.context , Message::class.java).apply {
                        Toast.makeText(view.context , txt_id.text.toString() , Toast.LENGTH_SHORT).show()

                        putExtra(name, txt_id.text.toString())
                    })
                }
            })
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): profileAdapter.profileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view , parent , false)

        return profileViewHolder(view)
    }

    override fun onBindViewHolder(holder: profileAdapter.profileViewHolder, position: Int) {
        var index = holder.position
        Log.d(TAG , profileList.get(index).tag+" tag ")

        Picasso.get().load(profileList.get(index).photoUrl).into(holder.profilePic)
        holder.nameText.text = profileList.get(index).name
        holder.txt_id.text = profileList.get(index).userID
        holder.tagTxt.text = profileList.get(index).tag
    }

    override fun getItemCount(): Int {
        Log.d(TAG,"Size" + profileList.size)
        return profileList.size
    }

}

data class profileInfo(
    val name:String,
    val photoUrl:String,
    val userID:String,
    val tag:String ,
)