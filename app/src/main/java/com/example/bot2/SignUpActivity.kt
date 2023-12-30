package com.example.bot2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.example.bot2.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // this line is used to disable dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        passwordEditTextSetup()
        confirmPasswordEditTextSetup()
        windowSetup()
        binding.signUpBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    // this function is called to setup the password edit text
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
                binding.passwordEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordEt.clearFocus()
            }
        }
    }

    // this function is called to setup the confirm password edit text
    fun confirmPasswordEditTextSetup(){
        binding.showConfirmPasswordIv.setOnClickListener {
            if (isConfirmPasswordVisible) {
                isConfirmPasswordVisible = false
                (it as ImageView).setImageResource(R.drawable.ic_show_password)
                binding.confirmPasswordEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.confirmPasswordEt.clearFocus()
            }
            else {
                isConfirmPasswordVisible = true
                (it as ImageView).setImageResource(R.drawable.ic_hide_password)
                binding.confirmPasswordEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.confirmPasswordEt.clearFocus()
            }
        }
    }


    // this function is called to setup the window status bar and navigation bar
    fun windowSetup() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

}