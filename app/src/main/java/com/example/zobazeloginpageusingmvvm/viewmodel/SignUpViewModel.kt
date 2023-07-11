package com.example.zobazeloginpageusingmvvm.viewmodel

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zobazeloginpageusingmvvm.view.AuthenticationActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {
         lateinit var firebaseAuth: FirebaseAuth

        fun initializeFirebaseAuth() {
            firebaseAuth = FirebaseAuth.getInstance()
        }



    fun createUserWithEmailAndPassword(email: String, password: String): LiveData<Boolean> {
        val createUserSuccessLiveData = MutableLiveData<Boolean>()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                createUserSuccessLiveData.value = task.isSuccessful
            }
        return createUserSuccessLiveData
    }


}
