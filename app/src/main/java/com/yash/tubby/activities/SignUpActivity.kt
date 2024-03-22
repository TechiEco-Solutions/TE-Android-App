package com.teapp.tubby.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.teapp.tubby.R
import com.teapp.tubby.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        //Initialize Firebase auth
        auth = FirebaseAuth.getInstance()

        binding.apply {
            tvSignIn.setOnClickListener {
                val intent = Intent(applicationContext, SignInActivity::class.java)
                startActivity(intent)
            }
            buttonSignUp.setOnClickListener {
                //get text from edit text field
                val email = etEmailAddress.text.toString()
                val password = etPassword.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {

                    textFieldEmailAddress.error = null
                    textFieldPassword.error = null

                    if (isValidEmail(email)) {
                        textFieldEmailAddress.error = null
                        if (isValidPassword(password)) {
                            textFieldPassword.error = null
                            signUpWithEmailAndPassword(email, password)
                        } else {
                            textFieldPassword.error =
                                "the length of password should be at least 8 and must contains atleast 1 numeric ,1 upper case letter ,1 lower case letter and 1 special symbol."
                            textFieldPassword.requestFocus()
                        }
                    } else {
                        textFieldEmailAddress.error = "Invalid Email!"
                    }
                } else {
                    if (password.isEmpty()) {
                        textFieldPassword.error = "Password can't be empty."
                    } else {
                        textFieldEmailAddress.error = "Email can't be empty."
                    }
                }
            }
        }

        }

        //checking if the email fulfils the required criteria
        // i.e. , is email has only alpha-numeric characters and ends with "@gmail.com"
        private fun isValidEmail(email: String): Boolean {
            val emailPattern = "^[A-Za-z0-9.]+@gmail.com$"
            return Pattern.matches(emailPattern, email)
        }

        //checking if the password fulfils the required criteria
        // i.e. , is password has At least 8 characters, no spaces, 1 numeric, 1 uppercase, 1 lowercase, 1 special symbol
        private fun isValidPassword(password: String): Boolean {
            // Password pattern: At least 8 characters, no spaces, 1 numeric, 1 uppercase, 1 lowercase, 1 special symbol
            val passwordPattern =
                "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
            return Pattern.matches(passwordPattern, password)
        }

        //function to start and perform manual sign-in with (Email,Password) process
        private fun signUpWithEmailAndPassword(email: String, password: String) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign-up success
                        val user = auth.currentUser
                        val userEmail = user?.email
                        // Pass the email to the next activity
                        val intent = Intent(this, SignInActivity::class.java)
                        intent.putExtra("userEmail", userEmail)
                        startActivity(intent)
                        finish()

                        Toast.makeText(
                            applicationContext,
                            "Sign-up successful!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Sign-up failed
                        if (task.exception.toString()
                                .equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")
                        ) {
                            Toast.makeText(
                                applicationContext,
                                " The email address is already in use by another account. ",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Sign-up failed. Please try again. ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Log.e("yo", "${task.exception?.toString()}")
                    }
                }
        }


    }
