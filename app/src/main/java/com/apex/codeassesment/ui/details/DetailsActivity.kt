package com.apex.codeassesment.ui.details

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.Coordinates
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ActivityDetailsBinding
import com.apex.codeassesment.ui.location.LocationActivity
import com.apex.codeassesment.ui.location.LocationActivity.Companion.USER_LATITUDE_KEY
import com.apex.codeassesment.ui.location.LocationActivity.Companion.USER_LONGITUDE_KEY
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {
    companion object {
        const val SAVED_USER_KEY = "saved-user-key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = IntentCompat.getParcelableExtra(intent, SAVED_USER_KEY, User::class.java)

        user?.let {
            binding.apply {
                detailsName.text = getString(R.string.details_name, it.name?.first, it.name?.last)
                detailsEmail.text = getString(R.string.details_email, user.email)
                detailsAge.text = getString(R.string.details_age, user.dob?.age)

                val coordinates = user.location?.coordinates
                detailsLocation.text = getString(
                    R.string.details_location, coordinates?.latitude, coordinates?.longitude
                )

                Glide.with(this@DetailsActivity)
                    .load(it.picture?.large)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.broken_image)
                    .centerCrop()
                    .into(detailsImage)

                detailsLocationButton.setOnClickListener {
                    navigateLocation(coordinates)
                }
            }
        }
    }

    private fun navigateLocation(coordinates: Coordinates?) = coordinates?.let {
        val intent = Intent(this, LocationActivity::class.java)
            .putExtra(USER_LATITUDE_KEY, coordinates.latitude)
            .putExtra(USER_LONGITUDE_KEY, coordinates.longitude)
        startActivity(intent)
    }

}