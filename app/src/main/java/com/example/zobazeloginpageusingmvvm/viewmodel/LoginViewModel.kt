package com.example.zobazeloginpageusingmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    lateinit var auth: FirebaseAuth

    fun initializeFirebaseAuth() {
        auth = FirebaseAuth.getInstance()
    }

    fun signInWithEmailAndPassword(email: String, password: String): LiveData<Boolean> {
        val signInSuccessLiveData = MutableLiveData<Boolean>()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                signInSuccessLiveData.value = task.isSuccessful
            }
        return signInSuccessLiveData
    }
}
