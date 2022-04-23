package com.dhiva.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dhiva.storyapp.data.local.StoryDatabase
import com.dhiva.storyapp.data.remote.ApiService
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.fake.FakeAuthPreferences
import com.dhiva.storyapp.fake.FakeStoryRepository
import com.dhiva.storyapp.utils.DataDummy
import com.dhiva.storyapp.utils.MainCoroutineRule
import com.dhiva.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var storyDatabase: StoryDatabase

    @Mock
    private lateinit var pref: DataStore<Preferences>
    private lateinit var authPreferences: FakeAuthPreferences
    private lateinit var storyRepository: FakeStoryRepository
    private lateinit var loginViewModel: LoginViewModel


    @Before
    fun setUp() {
        authPreferences = FakeAuthPreferences(pref)
        storyRepository = FakeStoryRepository(apiService, storyDatabase)
        loginViewModel = LoginViewModel(storyRepository, authPreferences)
    }

    @Test
    fun `when login should not null and return success`() = runTest {
        storyRepository.state = Resource.Success(0)
        val actualResult = loginViewModel.login("tes", "tes").getOrAwaitValue()

        assertNotNull(actualResult)
        assertTrue(actualResult is Resource.Success)
        assertEquals(actualResult.data, DataDummy.generateDummyLoginResult())
    }

    @Test
    fun `when login should return loading`() = runTest {
        storyRepository.state = Resource.Loading()
        val actualResult = loginViewModel.login("tes", "tes").getOrAwaitValue()

        assertNotNull(actualResult)
        assertTrue(actualResult is Resource.Loading)
    }

    @Test
    fun `when login should return error`() = runTest {
        storyRepository.state = Resource.Error("Error")
        val actualResult = loginViewModel.login("tes", "tes").getOrAwaitValue()

        assertNotNull(actualResult)
        assertTrue(actualResult is Resource.Error)
        assertEquals("Error", actualResult.message)
    }

    @Test
    fun `when get auth session called should return user and token not null`() {
        authPreferences.state = Resource.Success(0)
        val actualResult = loginViewModel.getAuthSession().getOrAwaitValue()

        assertNotNull(actualResult)
        assertEquals(DataDummy.generateDummyUser().token, actualResult.token)
    }

    @Test
    fun `when get auth session called should return user and token is null`() {
        authPreferences.state = Resource.Error("Error")
        val actualResult = loginViewModel.getAuthSession().getOrAwaitValue()

        assertNotNull(actualResult)
        assertEquals(null, actualResult.token)
    }

    @Test
    fun `save auth should called when set auth`() = runTest {
        authPreferences.isSaveCalled = false
        loginViewModel.setAuthSession(DataDummy.generateDummyUser())

        assertEquals(true, authPreferences.isSaveCalled)
    }
}