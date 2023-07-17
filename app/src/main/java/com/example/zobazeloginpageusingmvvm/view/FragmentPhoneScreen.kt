package com.example.zobazeloginpageusingmvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.zobazeloginpageusingmvvm.R
import com.example.zobazeloginpageusingmvvm.viewmodel.PhoneAuthViewModel
import com.google.firebase.auth.FirebaseUser

class FragmentPhoneScreen: Fragment(R.layout.fragment_phonescreen)

{
    private lateinit var authViewModel: PhoneAuthViewModel


    private lateinit var nameTextView: EditText
    private lateinit var phoneNumberTextView: EditText
    private lateinit var sendOtpButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_phonescreen, container, false)

        authViewModel = ViewModelProvider(requireActivity())[PhoneAuthViewModel::class.java]
        authViewModel.firebaseAuth
        var otpFragment=FragmentOTPScreen()
        nameTextView = view.findViewById(R.id.nameTextView)
        phoneNumberTextView = view.findViewById(R.id.phoneNumberTextView)
        sendOtpButton = view.findViewById(R.id.sendOtpButton)
        progressBar = view.findViewById(R.id.progressBar)

        sendOtpButton.setOnClickListener {

            val name = nameTextView.text.toString().trim()
            val phoneNumber = phoneNumberTextView.text.toString().trim()

            val otpScreenFragment=FragmentOTPScreen().apply {
                arguments=Bundle().apply {
                    putString("number",phoneNumber)
                    putString("name",name)
                }
            }
            otpFragment=otpScreenFragment

            if (name.isEmpty()) {
                nameTextView.error = "Please enter your name"
                nameTextView.requestFocus()
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()) {
                phoneNumberTextView.error = "Please enter your phone number"
                phoneNumberTextView.requestFocus()
                return@setOnClickListener
            }

            // Generate OTP by calling sendOtp function in AuthViewModel
            authViewModel.sendOtp(phoneNumber, this.requireActivity())
        }

        authViewModel.isLoading.observe(this.viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        authViewModel.otpVerificationStatus.observe(viewLifecycleOwner) { isVerified ->
            if (isVerified) {

                Toast.makeText(context, "Verification Successful", Toast.LENGTH_SHORT).show()

                val fragmentManager = activity?.supportFragmentManager
                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.fragmentContainer, otpFragment)
                fragmentTransaction?.commit()

            } else {

                Toast.makeText(context, "Verification Failed Try Again", Toast.LENGTH_SHORT).show()

                //Show error message in the logcat
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = authViewModel.firebaseAuth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {

            Toast.makeText(context,"Already Logged In", Toast.LENGTH_SHORT).show()

           val intent = Intent(context, HomePageActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

    }


}