package com.example.tubby.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.tubby.R
import com.example.tubby.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //For Status Bar Color
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.main)


        navigationDrawer()

    }

    private fun navigationDrawer() {

        //Setting Up Navigation Drawer
        toggle = ActionBarDrawerToggle(this, binding.mainDrawerLayout, 0, 0)
        binding.mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.mainMenuImageView.setOnClickListener(this)

        //Setting Clicks
        val navigationView = findViewById<NavigationView>(R.id.main_navigation_view)
        val headerView = navigationView.getHeaderView(0)
        val tubbyDo = headerView.findViewById<ImageView>(R.id.nav_header_tubby_do_image_view)
        val emptyTubby = headerView.findViewById<ImageView>(R.id.nav_header_empty_tubby_image_view)
        val addDevice = headerView.findViewById<ImageView>(R.id.nav_header_add_device_image_view)
        val preferences = headerView.findViewById<ImageView>(R.id.nav_header_preferences_image_view)
        val close = headerView.findViewById<ImageView>(R.id.nav_header_close_image_view)

        tubbyDo.setOnClickListener {

            Toast.makeText(this, "Tubby Do", Toast.LENGTH_SHORT).show()
        }
        emptyTubby.setOnClickListener {

            Toast.makeText(this, "Empty Tubby", Toast.LENGTH_SHORT).show()
        }
        addDevice.setOnClickListener {

            Toast.makeText(this, "Add Device", Toast.LENGTH_SHORT).show()
        }
        preferences.setOnClickListener {

            Toast.makeText(this, "Preference", Toast.LENGTH_SHORT).show()
        }
        close.setOnClickListener {

            binding.mainDrawerLayout.closeDrawer(binding.mainNavigationView)
        }
    }

    override fun onClick(view: View?) {

        //For Opening Drawer On Image Click
        when (view?.id) {
            R.id.main_menu_image_view -> {
                binding.mainDrawerLayout.openDrawer(binding.mainNavigationView)
            }
        }
    }
}