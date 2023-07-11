package com.example.zobazeloginpageusingmvvm.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.zobazeloginpageusingmvvm.R
import com.google.firebase.auth.FirebaseAuth

class HomePageActivity : AppCompatActivity() {

    private lateinit var logOutButton: Button
    private lateinit var userNameTv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        //display the user's name in the text view
        val userName:String?=intent.getStringExtra("userName")
        userNameTv=findViewById(R.id.userName)
        userNameTv!!.text=userName

        logOutButton=findViewById(R.id.logoutBtn)

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
            val intent= Intent(this,SignUpScreen::class.java)
            startActivity(intent)
            finish()
        }





    }
}