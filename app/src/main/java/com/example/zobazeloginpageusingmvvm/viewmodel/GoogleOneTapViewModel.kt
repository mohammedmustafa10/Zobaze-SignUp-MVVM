package com.example.zobazeloginpageusingmvvm.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zobazeloginpageusingmvvm.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import android.content.res.Resources
import android.provider.Settings.System.getString
import android.widget.ProgressBar


class GoogleOneTapViewModel(application: Application) : AndroidViewModel(application) {
    private var googleSignInClient: GoogleSignInClient
    private val _signInResult = MutableLiveData<SignInResult>()
    val signInResult: LiveData<SignInResult> = _signInResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(application, gso)
    }

    fun oneTapSignIn(activity: Activity) {
        _isLoading.value = true
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun handleSignInResult(requestCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                _signInResult.value = SignInResult(false, null, e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        _isLoading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    _signInResult.value = SignInResult(true, user, null)

                } else {
                    _signInResult.value = SignInResult(false, null, task.exception)
                        _isLoading.value = false
                }
            }
    }

    data class SignInResult(
        val isSuccessful: Boolean,
        val user: FirebaseUser?,
        val exception: Exception?
    )

    companion object {
        private const val RC_SIGN_IN = 123
    }
}
