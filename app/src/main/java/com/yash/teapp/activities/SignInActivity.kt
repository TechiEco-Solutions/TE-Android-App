package com.anshita.teapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yash.teapp.R
import com.yash.teapp.databinding.ActivitySignInBinding
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity() {

    // databinding(A Jetpack Architecture Component) is used.
    private lateinit var binding:ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            googleSignInLayout.setOnClickListener {
                //calling the below function to start Google sign in process
                startGoogleSignIn()
            }

            buttonSignIn.setOnClickListener {
                val email = binding.etPhone.text.toString()
                val password = binding.etPassword.text.toString()

                if (validateInput(email, password)) {
                    signIn(email, password)
                }
            }

        }

    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            showToast("Email cannot be empty")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter valid email address")
            return false
        }

        if (password.isEmpty()) {
            showToast("Password cannot be empty")
            return false
        }

        return true
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showToast("Sign in successful")
                    navigateToMainActivity()
                    finish()
                } else {
                    showToast("Sign in failed: ${task.exception?.message}")
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
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        return Pattern.matches(passwordPattern, password)
    }

    //function to start and perform manual sign-in with (Email,Password) process
    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-up success
                    val user = auth.currentUser
                    val userEmail = user?.email

                    //creating a sharedPreferences to save that now the user has sign in
                    //so when the user will open the app next time , then user will directly navigated to the main screen
                    // instead of login screen
                    val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isUserLogin", true)
                    editor.apply()

                    // Pass the email to the next activity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userEmail", userEmail)
                    startActivity(intent)
                    finish()

                    Toast.makeText(applicationContext, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                } else {
                    // Sign-up failed
                    if(task.exception.toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")){
                        Toast.makeText(applicationContext, " The email address is already in use by another account. ", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "Sign-up failed. Please try again. ", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("yo", "${task.exception.toString()}")
                }
            }
    }

    //function to start and perform google sign-in process
    private fun startGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    Log.w("GoogleSignIn", "Google sign in failed", e)
                }
            }

    }

    // Method for Creating account with google
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d("GoogleSignIn", "signInWithCredential:success")

                    val intent = Intent(this  , MainActivity::class.java)
                    if (user != null) {
                        intent.putExtra("user_email" , user.email)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}