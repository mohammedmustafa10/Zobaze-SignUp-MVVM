package com.example.zobazeloginpageusingmvvm.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.zobazeloginpageusingmvvm.R
import com.example.zobazeloginpageusingmvvm.viewmodel.PhoneAuthViewModel
import com.google.firebase.auth.FirebaseUser

class FragmentOTPScreen: Fragment(R.layout.fragment_otpscreen) {
    private lateinit var otpDigits: Array<EditText>
    private lateinit var verifyOtpButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var resendOtpTextView: TextView
    private lateinit var verificationIdOTP: String
    private lateinit var authViewModel: PhoneAuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_otpscreen, container, false)

        authViewModel = ViewModelProvider(requireActivity())[PhoneAuthViewModel::class.java]
        authViewModel.firebaseAuth
        val phoneNumber= arguments?.getString("number").toString()
        val name= arguments?.getString("name").toString()

        //verificationIdOTP= intent.getStringExtra("verificationId").toString()


        otpDigits = arrayOf(
            view.findViewById(R.id.otpDigit1EditText),
            view.findViewById(R.id.otpDigit2EditText),
            view.findViewById(R.id.otpDigit3EditText),
            view.findViewById(R.id.otpDigit4EditText),
            view.findViewById(R.id.otpDigit5EditText),
            view.findViewById(R.id.otpDigit6EditText)
        )

        verifyOtpButton = view.findViewById(R.id.verifyOtpButton)
        progressBar = view.findViewById(R.id.progressBar)
        resendOtpTextView = view.findViewById(R.id.resendOtpTextView)

        resendOtpTextView.setOnClickListener {

            // Disable the resend button to prevent multiple clicks
            resendOtpTextView.isEnabled = false

            // Show a loading indicator, such as a progress bar, to indicate the resend process
            progressBar.visibility = View.VISIBLE

            // Trigger the resend OTP action
            authViewModel.resendOTP(phoneNumber =phoneNumber, this.requireActivity())

            // Wait for a certain duration before enabling the resend button again
            val resendDelayMillis = 5000L // 5 seconds
            Handler().postDelayed({
                // Enable the resend button
                resendOtpTextView.isEnabled = true
                progressBar.visibility = View.GONE
            }, resendDelayMillis)

        }


        verifyOtpButton.setOnClickListener {
            val otp = StringBuilder()
            for (digit in otpDigits) {
                otp.append(digit.text.toString())
            }

            if (otp.length != 6) {
                // Invalid OTP length, show error message
                Toast.makeText(context,"Enter the 6 digit OTP", Toast.LENGTH_SHORT).show()
                // Adjust the error handling as per your requirement
                return@setOnClickListener
            }

            // Verify OTP by calling verifyOtp function in AuthViewModel
            authViewModel.verifyOtp(otp.toString())
        }

        authViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        authViewModel.otpVerificationStatus.observe(viewLifecycleOwner) { isVerified ->
            if (isVerified) {

                resendOtpTextView.visibility= View.GONE
                progressBar.visibility = View.GONE

                val intent = Intent(context, HomePageActivity::class.java)
                intent.putExtra("userName", name)
                startActivity(intent)
                requireActivity().finish()
                // OTP verification successful, proceed to the next screen
                // Adjust the logic as per your requirement
            } else {

                Toast.makeText(context,"OTP Verification Failed", Toast.LENGTH_SHORT).show()
                resendOtpTextView.visibility= View.VISIBLE
                progressBar.visibility = View.GONE
                // OTP verification failed, show error message
                // Adjust the error handling as per your requirement
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

        // Navigate to the HomeScreen
        val intent = Intent(context, HomePageActivity::class.java)
        startActivity(intent)
        requireActivity().finish()

    }


}