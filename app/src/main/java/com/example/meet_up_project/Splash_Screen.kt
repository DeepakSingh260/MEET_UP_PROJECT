package com.example.meet_up_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class Splash_Screen : AppCompatActivity() {

    private val SPLASH_SCREEN_TIMEOUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this , Login_Screen::class.java)
            startActivity(i)
            finish()
        },SPLASH_SCREEN_TIMEOUT)
    }
}