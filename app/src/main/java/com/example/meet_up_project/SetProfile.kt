package com.example.meet_up_project

import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class SetProfile : AppCompatActivity() {

    val SELECT_PICTURE = 100
    private var downloadUri : Uri? = null
    private lateinit var profile_img :ImageView
    private lateinit var change_img :Button
    private lateinit var save_changes : Button
    private lateinit var imageUri: Uri
    private lateinit var getName:EditText
    private lateinit var mCricket:CheckBox
    private lateinit var mFootball : CheckBox
    private val _db = FirebaseDatabase.getInstance().getReference("profiles/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_profile)

        Log.d(TAG ,Firebase.auth.currentUser!!.photoUrl.toString() )

        getName = findViewById(R.id.enter_name)
        profile_img =  findViewById(R.id.select_image)
        change_img = findViewById(R.id.change_img_btn)
        save_changes = findViewById(R.id.set_up_profile_btn)
        mCricket = findViewById(R.id.cricket_box)
        mFootball = findViewById(R.id.football_box)

        change_img.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(this@SetProfile , "open gallery" , Toast.LENGTH_SHORT).show()
                imageCloser()
            }
        })

        save_changes.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val profileUpdates = userProfileChangeRequest {
                    displayName = getName.text.toString()
                    photoUri = downloadUri
                }
                Firebase.auth.currentUser!!.updateProfile(profileUpdates).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this@SetProfile , "Changes Saved" , Toast.LENGTH_SHORT).show()

                    }
                }

                if (mFootball.isChecked){
                    _db.child(Firebase.auth.currentUser!!.uid+"/tag").setValue("Football")

                }else{
                    _db.child(Firebase.auth.currentUser!!.uid+"/tag").setValue("Cricket")
                }

                _db.child(Firebase.auth.currentUser!!.uid+"/name").setValue(getName.text.toString())
                _db.child(Firebase.auth.currentUser!!.uid+"/profileUrl").setValue(downloadUri.toString())
            }
        })

    }
    private fun imageCloser(){
        intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent , "Select Picture") , SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                imageUri = data?.data!!
                profile_img.setImageURI(imageUri)
                val ref  = FirebaseStorage.getInstance().reference?.child("profile_pic/"+ Firebase.auth.currentUser?.uid)

                val uploadTask = ref?.putFile(imageUri)
                val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot , Task<Uri>> {
                    if(!it.isSuccessful){
                        it.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { downloadUri = it.result!! }
            }
        }
    }

    companion object{
        const val TAG = "Profile"
    }
}