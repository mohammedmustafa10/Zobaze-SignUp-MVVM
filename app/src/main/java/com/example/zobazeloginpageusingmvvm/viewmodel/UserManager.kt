package com.example.zobazeloginpageusingmvvm.viewmodel

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth

object UserManager {
    private const val PREFS_NAME = "UserPrefs"
    private const val KEY_PHONE_AUTHENTICATED = "isPhoneAuthenticated"

    fun setPhoneAuthenticationStatus(context: Context, isAuthenticated: Boolean) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().putBoolean(KEY_PHONE_AUTHENTICATED, isAuthenticated).apply()
    }

    fun isPhoneAuthenticated(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean(KEY_PHONE_AUTHENTICATED, false)
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun clearUserData(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().clear().apply()
        FirebaseAuth.getInstance().signOut()
    }
}
