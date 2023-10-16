package com.yash.teapp.activities

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

        Handler().postDelayed(Runnable {
            val intent = Intent(this@SplashActivity , MainActivity::class.java)
            startActivity(intent)
            finish()
        },1000L)
    }
}