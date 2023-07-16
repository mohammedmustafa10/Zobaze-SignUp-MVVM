package com.example.zobazeloginpageusingmvvm.viewmodel

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuthViewModel : ViewModel() {

    private lateinit var verificationIds: String
    private lateinit var phoneAuthCredential: PhoneAuthCredential

    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val otpVerificationStatus: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var user=firebaseAuth.currentUser


    fun sendOtp(phoneNumber: String, activity: Activity) {
        isLoading.value = true

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    Toast.makeText(activity, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                    isLoading.value = false
                    // Save the verification ID and show the OTP input UI to the user
                    otpVerificationStatus.value = true
                    verificationIds = verificationId

                    Log.d("AuthVerification","Code Sent Successfully from OnCodeSent Function $verificationId")
                }
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Automatically handle verification if the SMS code is detected
                    // No user input required
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    // Handle verification failure
                    isLoading.value = false
                    otpVerificationStatus.value = false

                    Toast.makeText(activity, "On verification failed", Toast.LENGTH_SHORT).show()
                    Log.d("AuthVerification", "signInWithCredential:  ${exception.message}")
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendOTP(phoneNumber: String, activity: Activity){
        sendOtp(phoneNumber, activity)
    }

    fun verifyOtp(otp: String) {
        isLoading.value = true

        if (verificationIds.isNotEmpty()) {
            phoneAuthCredential = PhoneAuthProvider.getCredential(verificationIds, otp)
            signInWithPhoneAuthCredential(phoneAuthCredential)
        }
        else {
            // Handle the case where verificationId is not initialized
            isLoading.value = false
            otpVerificationStatus.value = false
            Log.d("AuthVerification", "Verification ID not initialized")
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //val user = task.result?.user
                    user=firebaseAuth.currentUser

                    isLoading.value = false
                    otpVerificationStatus.value = task.isSuccessful
                }else {
                    Log.d("AuthVerification", "signInWithCredential: ${task.exception?.message}")
                }
                Log.d("AuthVerification", "signInWithCredential: ${task.isSuccessful}")
            }
    }
}
