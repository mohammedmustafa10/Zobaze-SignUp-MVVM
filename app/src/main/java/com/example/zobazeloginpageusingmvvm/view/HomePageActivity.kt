package com.example.zobazeloginpageusingmvvm.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.zobazeloginpageusingmvvm.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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

           //google sign out
              val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

           val googleSignInClient = GoogleSignIn.getClient(this, gso)
              googleSignInClient.signOut()
           val sharedPreferences=getSharedPreferences("AuthPrefs", MODE_PRIVATE)
           sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
            FirebaseAuth.getInstance().signOut()
            finish()
            val intent= Intent(this,SignUpScreen::class.java)
            startActivity(intent)
            finish()
        }





    }
}