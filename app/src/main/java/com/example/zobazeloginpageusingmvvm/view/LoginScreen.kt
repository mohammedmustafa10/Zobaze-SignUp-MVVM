package com.example.zobazeloginpageusingmvvm.view

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.zobazeloginpageusingmvvm.R
import com.example.zobazeloginpageusingmvvm.viewmodel.GoogleOneTapViewModel
import com.example.zobazeloginpageusingmvvm.viewmodel.LoginViewModel
import com.example.zobazeloginpageusingmvvm.viewmodel.PhoneAuthViewModel
import com.google.firebase.auth.FirebaseUser

class LoginScreen : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var googleOneTapViewModel:GoogleOneTapViewModel
    private lateinit var phoneAuthViewModel: PhoneAuthViewModel
    private lateinit var signUp:TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var callButton:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)





        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.initializeFirebaseAuth()

        googleOneTapViewModel=ViewModelProvider(this)[GoogleOneTapViewModel::class.java]

        phoneAuthViewModel=ViewModelProvider(this)[PhoneAuthViewModel::class.java]



        val loginButton: Button = findViewById(R.id.btn_Login)
        val googleOneTapButton:ImageView=findViewById(R.id.GoogleOneTapButton)
        callButton=findViewById(R.id.CallButton)
        signUp=findViewById(R.id.signUpText)
        progressBar = findViewById(R.id.progressBar)

        signUp.setOnClickListener{
            progressBar.visibility= ProgressBar.VISIBLE
            val intent=Intent(this,SignUpScreen::class.java)
            startActivity(intent)
            progressBar.visibility= ProgressBar.INVISIBLE
        }

        callButton.setOnClickListener{
            progressBar.visibility= ProgressBar.VISIBLE
            val intent=Intent(this,PhoneScreen::class.java)
            startActivity(intent)
            progressBar.visibility= ProgressBar.INVISIBLE
        }

        googleOneTapButton.setOnClickListener{
            // Calling the one-tap sign-in function from GoogleOneTapViewModel
            googleOneTapViewModel.oneTapSignIn(this)
            progressBar.visibility= ProgressBar.VISIBLE
        }

        googleOneTapViewModel.signInResult.observe(this) { result ->
            if (result.isSuccessful) {
                // Sign-In was successful, handle the authenticated user
                progressBar.visibility= ProgressBar.VISIBLE
                val user = result.user
                updateUI(user)
            } else {
                progressBar.visibility= ProgressBar.INVISIBLE
                val exception = result.exception
                Log.e(TAG, "Sign-In failed: $exception")
            }
        }

        googleOneTapViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }




        loginButton.setOnClickListener {
            progressBar.visibility= ProgressBar.VISIBLE
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
                                progressBar.visibility= ProgressBar.INVISIBLE
                            }
                        }
                }

                else{
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    progressBar.visibility= ProgressBar.INVISIBLE
                    return@setOnClickListener
                }
            }
            else{
                Toast.makeText(this, "Please Enter a Valid Email", Toast.LENGTH_SHORT).show()
                progressBar.visibility= ProgressBar.INVISIBLE
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
        progressBar.visibility= ProgressBar.INVISIBLE
        finish()
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    public override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val phoneNumber= sharedPreferences.getString("phoneNumber", "").toString()

        if (isLoggedIn) {
            val intent = Intent(this, HomePageActivity::class.java)
            val userName = sharedPreferences.getString("userName", "")
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("userName", userName)
            startActivity(intent)
            finish()
        }


        val currentUser = loginViewModel.auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the sign-in result to the GoogleOneTapViewModel
        googleOneTapViewModel.handleSignInResult(requestCode, data)
    }


}
