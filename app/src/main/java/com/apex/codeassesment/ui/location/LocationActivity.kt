package com.apex.codeassesment.ui.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.apex.codeassesment.R
import com.apex.codeassesment.databinding.ActivityLocationBinding
import com.apex.codeassesment.utils.toMiles
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationActivity : AppCompatActivity() {

    companion object {
        const val USER_LATITUDE_KEY = "user-latitude-key"
        const val USER_LONGITUDE_KEY = "user-longitude-key"
        const val REQUEST_CODE = 100
    }

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val latitudeRandomUser = intent.getStringExtra(USER_LATITUDE_KEY)?.toDoubleOrNull()
        val longitudeRandomUser = intent.getStringExtra(USER_LONGITUDE_KEY)?.toDoubleOrNull()
        calculateAndUpdateDistanceToUserLocation(latitudeRandomUser, longitudeRandomUser)

        binding.locationRandomUser.text =
            if (latitudeRandomUser != null && longitudeRandomUser != null)
                getString(R.string.location_random_user, latitudeRandomUser, longitudeRandomUser)
            else
                getString(R.string.location_coordinates_error)


        binding.locationCalculateButton.setOnClickListener {
            calculateAndUpdateDistanceToUserLocation(latitudeRandomUser, longitudeRandomUser)
        }
    }

    private fun calculateAndUpdateDistanceToUserLocation(
        userLatitude: Double?,
        userLongitude: Double?
    ) {
        // Check if location permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }

        if (userLatitude == null || userLongitude == null) {
            binding.locationDistance.text = getString(R.string.location_coordinates_error)
            return
        }

        // Get the last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLocation = Location("UserLocation")
                userLocation.latitude = userLatitude
                userLocation.longitude = userLongitude
                val distance = it.distanceTo(userLocation).toMiles()
                binding.locationDistance.text = getString(R.string.location_result_miles, distance)
                binding.locationPhone.text =
                    getString(R.string.location_phone, it.latitude, it.longitude)
            }
        }
    }
}
