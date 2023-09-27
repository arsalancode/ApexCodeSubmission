package com.apex.codeassesment.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class UserTest {

    @Test
    fun testUserProperties() {
        // Create a user with specific properties
        val user = User(
            gender = "Male",
            name = mock(Name::class.java),
            location = mock(Location::class.java),
            email = "test@example.com",
            login = mock(Login::class.java),
            dob = mock(Dob::class.java),
            registered = mock(Dob::class.java),
            phone = "123-456-7890",
            cell = "987-654-3210",
            id = mock(Id::class.java),
            picture = mock(Picture::class.java),
            nat = "US"
        )

        // Assert that the user properties are set correctly
        assertEquals("Male", user.gender)
        assertEquals("test@example.com", user.email)
        assertEquals("123-456-7890", user.phone)
        assertEquals("987-654-3210", user.cell)
        assertEquals("US", user.nat)
    }

    @Test
    fun testCreateRandomUser() {
        // Create a random user
        val randomUser = User.createRandom()

        assert(!randomUser.email.isNullOrEmpty())
        assert(randomUser.location != null)
        assert(randomUser.name != null)
        assert(randomUser.dob != null)
    }
}
