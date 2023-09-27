package com.apex.codeassesment.data.local

import android.content.SharedPreferences
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PreferencesManagerTest {

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var preferencesManager: PreferencesManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        preferencesManager = PreferencesManager(sharedPreferences)
    }

    @Test
    fun saveUser_Success() {
        val user = "JohnDoe"

        // Mock the SharedPreferences.Editor
        val editor = mock(SharedPreferences.Editor::class.java)

        // Mock the behavior of the editor
        `when`(editor.putString(eq(PreferencesManager.SAVED_USER), eq(user))).thenReturn(editor)
        doNothing().`when`(editor).apply() // Handle void method with doNothing

        // Mock the behavior of sharedPreferences
        `when`(sharedPreferences.edit()).thenReturn(editor)

        // Call the saveUser method
        preferencesManager.saveUser(user)

        // Verify that putString and apply were called
        verify(editor).putString(PreferencesManager.SAVED_USER, user)
        verify(editor).apply()
    }


    @Test
    fun loadUser_ReturnsUser() {
        val user = "JohnDoe"

        // Mock the SharedPreferences
        `when`(sharedPreferences.getString(PreferencesManager.SAVED_USER, null)).thenReturn(user)

        // Call the loadUser method
        val loadedUser = preferencesManager.loadUser()

        // Verify that it returns the expected user
        assertEquals(user, loadedUser)
    }

    @Test
    fun loadUser_ReturnsNull() {
        // Mock the SharedPreferences
        `when`(sharedPreferences.getString(PreferencesManager.SAVED_USER, null)).thenReturn(null)

        // Call the loadUser method
        val loadedUser = preferencesManager.loadUser()

        // Verify that it returns null
        assertNull(loadedUser)
    }
}
