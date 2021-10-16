package com.example.meet_up_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class Login_Screen : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var sign_up:TextView
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var logIn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

//        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }

        email = findViewById(R.id.email_login)
        password = findViewById(R.id.password_login)
        logIn = findViewById(R.id.button_login)


        sign_up = findViewById(R.id.sign_up_txt)

        sign_up.setOnClickListener {
                Toast.makeText(applicationContext , "Tap" , Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@Login_Screen , Sign_up::class.java))
            }

        logIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val emailT = email.text.toString()
                val passwordT = password.text.toString()

                if (TextUtils.isEmpty(emailT ) ){
                    Toast.makeText(applicationContext , "Enter Email" , Toast.LENGTH_SHORT).show()
                    return
                }
                if (TextUtils.isEmpty(passwordT ) ){
                    Toast.makeText(applicationContext , "Enter Password" , Toast.LENGTH_SHORT).show()
                    return
                }
                auth.signInWithEmailAndPassword(emailT ,passwordT).addOnCompleteListener(this@Login_Screen){task->
                    if(!task.isSuccessful){
                        Toast.makeText(applicationContext , "Auth Failed" , Toast.LENGTH_SHORT).show()
                    }
                    else{
                        startActivity(Intent(applicationContext , MainActivity::class.java))
                        finish()
                    }
                }
            }
        })

    }
}