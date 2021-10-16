package com.example.meet_up_project

import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class profile : Fragment() {

    private lateinit var profile_name: TextView
    private lateinit var profile_image : ImageView
    private lateinit var tag:TextView
    private val user = Firebase.auth.currentUser
    private val _db = FirebaseDatabase.getInstance().getReference("profiles/"+user!!.uid)
    private lateinit var change:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onStart() {
        super.onStart()
        change = requireView().findViewById(R.id.edit_profile)
        profile_name = requireView().findViewById(R.id.profile_name)
        profile_image = requireView().findViewById(R.id.profile_image)
        tag  = requireView().findViewById(R.id.profile_tag)
        change.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(requireContext() , SetProfile::class.java ))
            }
        })

        _db.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d(TAG ,snapshot.toString() )
//                for (post in snapshot.children.iterator()){
                    Log.d(TAG , snapshot.toString())
                    val photo = snapshot.child("profileUrl").value.toString()
                    val name = snapshot.child("name").value.toString()
                    val Tag = snapshot.child("tag").value.toString()

                    profile_name.text = name
                    tag.text = Tag
                    Picasso.get().load(photo).into(profile_image)
//                }
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
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    companion object {

    }
}