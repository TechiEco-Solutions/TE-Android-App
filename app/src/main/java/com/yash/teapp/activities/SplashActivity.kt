package com.yash.teapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.view.WindowCompat
import com.yash.teapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        WindowCompat.setDecorFitsSystemWindows(
            window ,
            false
        )

        val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val isUserAlreadyLogIn = sharedPreferences.getBoolean("isUserLogin", false)

        Handler().postDelayed(Runnable {
            if(!isUserAlreadyLogIn){
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        },2000L)
    }
}