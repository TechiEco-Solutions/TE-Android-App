package com.example.bot2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.WindowManager
import android.widget.ImageView
import com.example.bot2.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignInBinding
    private var isPasswordVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        passwordEditTextSetup()
        windowSetup()
        binding.signInBtn.setOnClickListener {
            val intent = Intent(this, HomeActivty::class.java)
            startActivity(intent)
        }
    }

    fun passwordEditTextSetup(){
        binding.showPasswordIv.setOnClickListener {
            if (isPasswordVisible) {
                isPasswordVisible = false
                (it as ImageView).setImageResource(R.drawable.ic_show_password)
                binding.passwordEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.passwordEt.clearFocus()
            }
            else {
                isPasswordVisible = true
                (it as ImageView).setImageResource(R.drawable.ic_hide_password)
                binding.passwordEt.inputType =  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordEt.clearFocus()

            }
        }
    }

    fun windowSetup() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}