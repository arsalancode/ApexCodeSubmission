import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.local.PreferencesManager
import com.apex.codeassesment.data.model.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LocalDataSourceTest {

    @Mock
    private lateinit var preferencesManager: PreferencesManager

    private lateinit var moshi: Moshi
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        localDataSource = LocalDataSource(preferencesManager, moshi)
    }

    @Test
    fun loadUser_WhenUserIsSaved_ReturnsUser() {
        val user = User.createRandom()
        val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)

        // Mock the behavior of loading a serialized user
        val serializedUser = jsonAdapter.toJson(user)
        `when`(preferencesManager.loadUser()).thenReturn(serializedUser)

        // Call the loadUser method
        val loadedUser = localDataSource.loadUser()

        // Verify that it returns the expected user
        assertEquals(user, loadedUser)
    }

    @Test
    fun loadUser_WhenUserIsNotSaved_ReturnsRandomUser() {
        // Mock the behavior of not having a saved user
        `when`(preferencesManager.loadUser()).thenReturn(null)

        // Call the loadUser method
        val loadedUser = localDataSource.loadUser()

        // Verify that it returns a random user
        assertNotNull(loadedUser)
    }

    @Test
    fun saveUser_SavesUserCorrectly() {
        val user = User.createRandom()

        // Mock the behavior of saving a serialized user
        val jsonAdapter: JsonAdapter<User> = moshi.adapter(User::class.java)
        val serializedUser = jsonAdapter.toJson(user)

        // Call the saveUser method
        localDataSource.saveUser(user)

        // Verify that it saves the user correctly
        verify(preferencesManager).saveUser(serializedUser)
    }
}
