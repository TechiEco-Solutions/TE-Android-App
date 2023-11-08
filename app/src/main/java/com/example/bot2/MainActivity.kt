package com.example.bot2

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bot2.adapters.DrawerAdapter
import com.example.bot2.databinding.ActivityMainBinding
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.bot2.services.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationService = LocationService(this)

        drawerSetup()
        windowSetup()


        binding.powerBtnCard.setOnClickListener {
            requestLocationAndUpdateUI()
        }
    }

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
                this,
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

    fun windowSetup() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    private fun requestLocationAndUpdateUI() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationService.checkLocationSettingsAndStartLocationUpdates { location ->
                updateLocationUI(location)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LocationService.REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun updateLocationUI(location: Location?) {
        location?.let {
            Log.d("LocationLog", "Lat: ${it.latitude}\nLng: ${it.longitude}")
            Toast.makeText(this, "Lat: ${it.latitude}\nLng: ${it.longitude}", Toast.LENGTH_SHORT).show()
        } ?: Log.d("LocationLog", "Location is null")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationService.REQUEST_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                requestLocationAndUpdateUI()
            } else {
                showRationaleDialog()
            }
        }
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(this)
            .setMessage("This app needs location permission to function. Please enable it in settings.")
            .setPositiveButton("Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LocationService.REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                locationService.checkLocationSettingsAndStartLocationUpdates { location ->
                    location?.let {
                        Log.d("LocationLog", "Lat: ${it.latitude}\nLng: ${it.longitude}")
                    } ?: Log.d("LocationLog", "Location is null")
                    Toast.makeText(this,"lat: ${location?.latitude}\nlng: ${location?.longitude}",Toast.LENGTH_SHORT).show()
                }
            } else {
                // User chose not to make required location settings changes.
                Log.d("LocationLog", "User chose not to make required location settings changes.")
            }
        }
    }

}


