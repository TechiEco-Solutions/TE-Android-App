package com.yash.teapp.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.yash.teapp.R
import com.yash.teapp.adapters.CustomNavigationAdapter
import com.yash.teapp.dataClasses.CustomMenuItem
import com.yash.teapp.databinding.ActivityMainBinding
import java.net.NetworkInterface

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isWifiPermissionGranted:Boolean ?= false

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)

        // Check and request permissions at runtime
        if (hasPermission()) {
            // Permissions are granted, proceed with getting IP and MAC addresses
            isWifiPermissionGranted = true
        } else {
            // Permissions are not granted, request them
            requestPermission()
        }

        var email = intent.getStringExtra("user_email")

        if (email != null) {
            if(email.isNotEmpty()) {
                Toast.makeText(this, email, Toast.LENGTH_SHORT).show()
            }
        }

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
                if(isWifiPermissionGranted == true){
                    val ipAddress = getIPAddress()
                    val macAddress = getMacAddress()!!

                    Toast.makeText(applicationContext , "IP Address: $ipAddress \n MAC Address: $macAddress",Toast.LENGTH_LONG).show()
                }

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

    private fun getIPAddress(): String {
        // Obtain IP address using WifiManager
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo? = wifiManager.connectionInfo
        val ipAddress = wifiInfo?.ipAddress ?: 0
        return String.format("%d.%d.%d.%d", ipAddress and 0xff, ipAddress shr 8 and 0xff, ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff)
    }

    private fun getMACAddress(): String {
        // Obtain MAC address using WifiManager
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo? = wifiManager.connectionInfo
        return wifiInfo?.macAddress ?: "Not available"
    }

    fun getMacAddress(): String? {
        try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val hardwareAddress = networkInterface.hardwareAddress ?: continue

                val macStringBuilder = StringBuilder()
                for (byte in hardwareAddress) {
                    macStringBuilder.append(String.format("%02X:", byte))
                }

                if (macStringBuilder.isNotEmpty()) {
                    macStringBuilder.deleteCharAt(macStringBuilder.length - 1)
                    return macStringBuilder.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_WIFI_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_WIFI_STATE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            isWifiPermissionGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
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