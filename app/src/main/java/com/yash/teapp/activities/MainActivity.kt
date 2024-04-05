package com.anshita.teapp.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.yash.teapp.R
import com.yash.teapp.adapters.DrawerAdapter
import com.yash.teapp.dataClasses.DrawerItem
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


        drawerSetup()
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
    //drawer function for drawer

    fun drawerSetup() {


        val drawerItems = listOf(
            DrawerItem("Tubby Do", R.drawable.ic_tubby_do),
            DrawerItem("Empty Tubby", R.drawable.ic_empty_tubby),
            DrawerItem("Add Device", R.drawable.ic_add_device),
            DrawerItem("Preferences", R.drawable.ic_prefrences),
        )


        val drawerAdapter = DrawerAdapter(drawerItems)
        binding.drawerRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = drawerAdapter
        }

        //when an drawer item is clicked
        drawerAdapter.onClick = { itemPosition ->
            Toast.makeText(
                this@MainActivity,
                "Clicked on ${drawerItems[itemPosition].title}",
                Toast.LENGTH_SHORT
            ).show()
        }


        binding.drawerIconBtn.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val headerView: View = binding.navigationView.getHeaderView(0)
        val closeButton: ImageButton = headerView.findViewById(R.id.ic_drawer_close)


        closeButton.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
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