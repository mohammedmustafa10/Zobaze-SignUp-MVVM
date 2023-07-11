package com.example.zobazeloginpageusingmvvm.view

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.zobazeloginpageusingmvvm.R
import com.example.zobazeloginpageusingmvvm.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class LoginScreen : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.initializeFirebaseAuth()

        val loginButton: Button = findViewById(R.id.btn_Login)


        loginButton.setOnClickListener {
            val loginEmailTv: EditText = findViewById(R.id.loginEmail)
            val loginPasswordTv: EditText = findViewById(R.id.loginPassword)
            val loginEmail = loginEmailTv.text.toString().trim()
            val loginPassword = loginPasswordTv.text.toString().trim()


            if(isValidEmail(loginEmail)){

                if(loginEmail.isNotEmpty()&&loginPassword.isNotEmpty()){

                    loginViewModel.signInWithEmailAndPassword(loginEmail, loginPassword)
                        .observe(this) { signInSuccess ->
                            if (signInSuccess) {
                                // Sign in success, update UI
                                val user = loginViewModel.auth.currentUser
                                updateUI(user)
                            } else {
                                // Sign in failure, show error message
                                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }

                else{
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else{
                Toast.makeText(this, "Please Enter a Valid Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



        }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("userName", user?.displayName)
        Log.d("abc", user?.email.toString())
        Log.d("abc", user?.displayName.toString())
        Log.d("abc", user?.phoneNumber.toString())
        startActivity(intent)
        finish()
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}
