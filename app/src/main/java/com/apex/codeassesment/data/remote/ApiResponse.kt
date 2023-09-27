package com.apex.codeassesment.data.remote

import android.os.Parcelable
import com.apex.codeassesment.data.model.User
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ApiResponse(
    val results: List<User>?,
    val error: String? = null  // Information about any errors that occurred
) : Parcelable