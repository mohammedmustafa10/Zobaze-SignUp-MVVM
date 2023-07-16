package com.example.zobazeloginpageusingmvvm.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.zobazeloginpageusingmvvm.R
import com.example.zobazeloginpageusingmvvm.viewmodel.PhoneAuthViewModel


class PhoneScreen : AppCompatActivity() {
    override val viewModelStore by lazy { ViewModelStore() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_screen)

        val phoneScreenFragment=FragmentPhoneScreen()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer,phoneScreenFragment)
            commit()
        }
    }




}
