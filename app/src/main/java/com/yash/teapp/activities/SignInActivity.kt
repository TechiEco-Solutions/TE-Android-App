package com.yash.teapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    private lateinit var binding:ActivitySignInBinding

    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        binding.apply {
            googleSignInLayout.setOnClickListener {
                startGoogleSignIn()
            }

            buttonSignUp.setOnClickListener {
                val email:String = etPhone.text.toString()
                val password:String = etPassword.text.toString()
                if(email.isNotEmpty() && password.isNotEmpty()) {

                    textFieldPhone.error = null
                    textFieldPassword.error = null

                        if (isValidEmail(email)) {
                        textFieldPhone.error = null
                        if(isValidPassword(password)) {
                            textFieldPassword.error = null
                                signUpWithEmailAndPassword(email, password)
                        }else{
                            textFieldPassword.error = "the length of password should be at least 8 and must contains atleast 1 numeric ,1 upper case letter ,1 lower case letter and 1 special symbol."
                        }
                    } else {
                        textFieldPhone.error = "Invalid Email!"
                    }
                }else{
                    if(password.isEmpty()) {
                        textFieldPassword.error = "Password can't be empty."
                    }else{
                        textFieldPhone.error = "Email can't be empty."
                    }
                }
            }

        }

    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[A-Za-z0-9.]+@gmail.com$"
        return Pattern.matches(emailPattern, email)
    }

    private fun isValidPassword(password: String): Boolean {
        // Password pattern: At least 8 characters, no spaces, 1 numeric, 1 uppercase, 1 lowercase, 1 special symbol
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        return Pattern.matches(passwordPattern, password)
    }

    private fun signUpWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-up success
                    val user = auth.currentUser
                    val userEmail = user?.email

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


}