package com.apex.codeassesment.utils

import android.content.Context
import android.content.Intent
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.ui.details.DetailsActivity

const val METERS_TO_MILES_CONVERSION_UNIT = 0.000621371192

fun Context.navigateDetails(user: User) {
    val intent = Intent(this, DetailsActivity::class.java)
    intent.putExtra("saved-user-key", user)
    startActivity(intent)
}

fun Float.toMiles() = this * METERS_TO_MILES_CONVERSION_UNIT