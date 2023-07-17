package com.example.zobazeloginpageusingmvvm.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zobazeloginpageusingmvvm.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomePageActivity : AppCompatActivity() {

    private lateinit var logOutButton: Button
    private lateinit var userNameTv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        //display the user's name in the text view
        val userName:String?=intent.getStringExtra("userName")
        val phoneNumber:String=intent.getStringExtra("phoneNumber").toString()
        userNameTv=findViewById(R.id.userName)

        val firestore = FirebaseFirestore.getInstance()
        Log.d("pinball", "This is the Phone Number :$phoneNumber")
       val userDocRef = firestore.collection("users").document(phoneNumber)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val name = documentSnapshot.getString("name")
                    userNameTv.text = name
                    val sharedPreferences=getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("userName",name).apply()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this,"Firestore Name Retrieval Error! $e", Toast.LENGTH_SHORT).show()
                Log.d("Firestore", "$e")
            }



        //userNameTv.text=userName

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