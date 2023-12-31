package com.example.zobazeloginpageusingmvvm.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import android.util.Patterns
import androidx.lifecycle.ViewModelProvider
import com.example.zobazeloginpageusingmvvm.R
import com.example.zobazeloginpageusingmvvm.viewmodel.SignUpViewModel

class SignUpScreen : AppCompatActivity() {

    private lateinit var signUpViewModel: SignUpViewModel


    private lateinit var emailTv: EditText
    private lateinit var passwordTv: EditText
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var signUpButton: Button
    private lateinit var loginButtonMainScreen:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        signUpViewModel.initializeFirebaseAuth()

        //auth =FirebaseAuth.getInstance()
        signUpButton=findViewById(R.id.btn_signUp)
        emailTv=findViewById(R.id.email)
        passwordTv=findViewById(R.id.password)
        loginButtonMainScreen=findViewById(R.id.loginButtonMainScreen)

        loginButtonMainScreen.setOnClickListener{

            val intent=Intent(this,LoginScreen::class.java)
            startActivity(intent)
        }


        fun isValidEmail(email: String): Boolean {
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }


        signUpButton.setOnClickListener {

            email=emailTv.text.toString().trim()
            password=passwordTv.text.toString().trim()


            if(isValidEmail(email)){

                if(email.isNotEmpty()&&password.isNotEmpty()){

                    signUpViewModel.createUserWithEmailAndPassword(email, password).observe(this){
                        if(it){
                            val user=signUpViewModel.firebaseAuth.currentUser
                            sendVerificationEmail(user)
                        }
                        else{
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }


                    }

                   /* auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                sendVerificationEmail()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                //updateUI(null)
                            }
                        }*/
                }


                else{

                    Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

            }

            else{

                Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }




        }
    }

    private fun sendVerificationEmail(firebaseUser:FirebaseUser?) {
        firebaseUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification email sent to $email", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this, AuthenticationActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }

    private fun updateUI(user: FirebaseUser?) {
        val intent=Intent(this,HomePageActivity::class.java)
        intent.putExtra("userName",user?.displayName)
        startActivity(intent)
        finish()

    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = signUpViewModel.firebaseAuth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }
}