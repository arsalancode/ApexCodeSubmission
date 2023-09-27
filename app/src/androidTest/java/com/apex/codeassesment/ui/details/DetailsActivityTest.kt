package com.apex.codeassesment.ui.details

import android.content.Intent
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.Coordinates
import com.apex.codeassesment.data.model.Dob
import com.apex.codeassesment.data.model.Location
import com.apex.codeassesment.data.model.Name
import com.apex.codeassesment.data.model.Picture
import com.apex.codeassesment.data.model.User
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DetailsActivityTest {

    private lateinit var intent: Intent

    @Before
    fun setUp() {
        intent = Intent(ApplicationProvider.getApplicationContext(), DetailsActivity::class.java)
    }

    @Test
    fun testDetailsActivityDisplayUserData2() {
        val user = User(
            name = Name(first = "John", last = "Doe"),
            email = "john.doe@example.com",
            dob = Dob(age = 30),
            location = Location(coordinates = Coordinates(latitude = "12.345", longitude = "67.890")),
            picture = Picture(large = "https://example.com/john-doe.jpg")
        )

        intent.putExtra(DetailsActivity.SAVED_USER_KEY, user)

        val scenario = ActivityScenario.launch<DetailsActivity>(intent)

        scenario.onActivity { activity ->
            // Verify that the user's details are displayed in the activity
            val nameTextView = activity.findViewById<TextView>(R.id.details_name)
            val emailTextView = activity.findViewById<TextView>(R.id.details_email)
            val ageTextView = activity.findViewById<TextView>(R.id.details_age)
            val locationTextView = activity.findViewById<TextView>(R.id.details_location)

            activity.runOnUiThread {
                assertEquals("Name: John Doe", nameTextView.text.toString())
                assertEquals("Email: john.doe@example.com", emailTextView.text.toString())
                assertEquals("Age: 30", ageTextView.text.toString())
                assertEquals("Location: (12.345, 67.890)", locationTextView.text.toString())
            }
        }
    }

}