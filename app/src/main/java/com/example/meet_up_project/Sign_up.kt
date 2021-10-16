package com.example.meet_up_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Sign_up : AppCompatActivity() {

    private  lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var sign_up_btn:Button
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = Firebase.auth

        name = findViewById(R.id.name_sign_in)
        email = findViewById(R.id.email_sign_in)
        password  = findViewById(R.id.password_sign_in)
        sign_up_btn = findViewById(R.id.sign_up_btn)

        sign_up_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (v!=null){
                    signInFunc(email.text.toString().trim() , password.text.toString().trim())
                }
            }
        })

    }

    private fun signInFunc(email:String , password:String){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
            task->
            if(task.isSuccessful){
                Log.d(TAG , "Sign In With Email")
                val user = auth.currentUser
                startActivity(Intent(this , MainActivity::class.java))
            }else{
                Log.w(TAG , "Sign In Failure" , task.exception)
            }
        }

    }

    companion object{
        private const val TAG = "SignIn"
    }
}