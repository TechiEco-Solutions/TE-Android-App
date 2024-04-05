package com.anshita.teapp.activities

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

        //This is used for Splash Screen Activity to cover the entire screen- (the status bar too)
        WindowCompat.setDecorFitsSystemWindows(
            window ,
            false
        )

        //created a SharedPreferences to check if the user has already signed in earlier.
        val sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val isUserAlreadyLogIn = sharedPreferences.getBoolean("isUserLogin", false)

        Handler().postDelayed(Runnable {

            //If not signed in then navigating the user ti sign in screen
            if(!isUserAlreadyLogIn){
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
            //otherwise navigating the user to the main screen(Dashboard)
            else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        },2000L)
    }
}