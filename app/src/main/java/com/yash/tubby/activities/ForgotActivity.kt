package com.yash.tubby.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.teapp.tubby.R
import com.teapp.tubby.activities.SignInActivity
import com.teapp.tubby.databinding.ActivityForgotBinding

class ForgotActivity : AppCompatActivity() {


    private lateinit var auth:FirebaseAuth
    private lateinit var binding:ActivityForgotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.reset.setOnClickListener {
            resetData()
        }
        binding.back.setOnClickListener {
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun resetData() {
              if(binding.emailAddress.text!!.isEmpty()){
                  binding.emailAddress.setError("Required")
              }else{
                  sentEmail()
              }
    }

    private fun sentEmail() {
        auth = FirebaseAuth.getInstance()
        val  email = binding.emailAddress.text.toString()
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            Toast.makeText(this,"Please Check your Email",Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
                Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
            }

    }


}