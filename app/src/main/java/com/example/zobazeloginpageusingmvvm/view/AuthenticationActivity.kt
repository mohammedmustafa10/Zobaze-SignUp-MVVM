package com.example.zobazeloginpageusingmvvm.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.zobazeloginpageusingmvvm.R
import com.example.zobazeloginpageusingmvvm.viewmodel.SignUpViewModel

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var authenticationViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)


        val loginButton: Button = findViewById(R.id.btn_GoToLogin)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }
    }
}
