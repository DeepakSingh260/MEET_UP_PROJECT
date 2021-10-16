package com.example.meet_up_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottom_nav_bar:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav_bar = findViewById(R.id.main_nav_bar)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame,Home()).commit()
        }

        bottom_nav_bar.setOnNavigationItemSelectedListener{
            Toast.makeText(this , "TAP" , Toast.LENGTH_SHORT).show()
            when(it.itemId){
                R.id.profile->  supportFragmentManager.beginTransaction().apply { replace(R.id.frame,profile()).commit() }
                R.id.friends->  supportFragmentManager.beginTransaction().apply { replace(R.id.frame,friends()).commit() }
                R.id.home->  supportFragmentManager.beginTransaction().apply { replace(R.id.frame,Home()).commit() }
            }
            true
        }
    }
}