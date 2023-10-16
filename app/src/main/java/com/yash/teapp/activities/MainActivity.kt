package com.yash.teapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.yash.teapp.R
import com.yash.teapp.adapters.CustomNavigationAdapter
import com.yash.teapp.dataClasses.CustomMenuItem
import com.yash.teapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)

        binding.apply {

            setSupportActionBar(toolbar)
            supportActionBar?.title = ""

            customNavigationIcon.setOnClickListener {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }

            //toggle.syncState()

            // Create custom menu items
            val customMenuItems = listOf(
                CustomMenuItem(R.drawable.tubby_do, "Tubby Do"),
                CustomMenuItem(R.drawable.empty_tubby, "Empty Tubby"),
                CustomMenuItem(R.drawable.add_device_img, "Add Device"),
                CustomMenuItem(R.drawable.preferences_img, "Preferences")
            )
            // Add more custom menu items as needed

            // Create and set the custom adapter for the ListView
            val adapter = CustomNavigationAdapter(applicationContext, customMenuItems)
            customNavList.adapter = adapter

            // Set item click listener
            customNavList.setOnItemClickListener { _, _, position, _ ->

                val selectedItem = customMenuItems[position]
                // Handle item click based on the selected item
                when (selectedItem.text) {
                    "Tubby Do" -> {
                        // Handle Custom Item 1 click
                        Toast.makeText(applicationContext, "DO Clicked!", Toast.LENGTH_SHORT).show()
                    }
                    "Empty Tubby" -> {
                        // Handle Custom Item 2 click
                        Toast.makeText(applicationContext, "Delete Clicked!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    "Add Device" -> {
                        // Handle Custom Item 1 click
                        Toast.makeText(
                            applicationContext,
                            "Add Device Clicked!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    "Preferences" -> {
                        // Handle Custom Item 2 click
                        Toast.makeText(
                            applicationContext,
                            "Preferences Clicked!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // Add more cases for other custom items
                }
                drawerLayout.closeDrawer(GravityCompat.START)

            }

            //
            val buttonPressAnimation: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.button_press)

            btnPower.setOnClickListener {
                // Create the animation

                // Set the animation listener to reset the animation when it finishes
                buttonPressAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        // Animation started
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        // Reset the animation
                        btnPower.clearAnimation()
                    }

                    override fun onAnimationRepeat(animation: Animation) {
                        // Animation repeated
                    }
                })

                // Start the animation
                btnPower.startAnimation(buttonPressAnimation)

                // Perform actions when the button is clicked
            }

            btnSettings.setOnClickListener {
                // Create the animation

                // Set the animation listener to reset the animation when it finishes
                buttonPressAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        // Animation started
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        // Reset the animation
                        btnSettings.clearAnimation()
                    }

                    override fun onAnimationRepeat(animation: Animation) {
                        // Animation repeated
                    }
                })

                // Start the animation
                btnSettings.startAnimation(buttonPressAnimation)

                // Perform actions when the button is clicked
            }

            btnSchedule.setOnClickListener {

                buttonPressAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        // Animation started
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        // Reset the animation
                        btnSchedule.clearAnimation()
                    }

                    override fun onAnimationRepeat(animation: Animation) {
                        // Animation repeated
                    }
                })

                // Start the animation
                btnSchedule.startAnimation(buttonPressAnimation)

            }
        }
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}