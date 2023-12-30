package com.example.bot2.services

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


// this class is used to get the location of the user
class LocationService(private val activity: Activity) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

    fun checkLocationSettingsAndStartLocationUpdates(callback: (Location?) -> Unit) {
        val locationSettingsRequest = buildLocationSettingsRequest(buildLocationRequest())
        val settingsClient = LocationServices.getSettingsClient(activity)

        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                // All location settings are satisfied, start location updates
                getCurrentLocation(callback)
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed by showing the user a dialog
                    try {
                        exception.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error
                    }
                }
            }
    }

    private fun buildLocationRequest(): LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 0
        fastestInterval = 0
        numUpdates = 1
    }

    private fun buildLocationSettingsRequest(locationRequest: LocationRequest): LocationSettingsRequest {
        return LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
    }

    private fun getCurrentLocation(callback: (Location?) -> Unit) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            return
        }

        // Directly request for the current location
        requestNewLocationData(callback)
    }

    private fun requestNewLocationData(callback: (Location?) -> Unit) {
        val locationRequest = buildLocationRequest()

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    callback(locationResult.lastLocation)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }, activity.mainLooper)
        } catch (unlikely: SecurityException) {
            Log.e("LocationService", "Lost location permission. Could not request updates. $unlikely")
        }
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
        const val REQUEST_CHECK_SETTINGS = 2
    }
}
