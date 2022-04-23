package com.dhiva.storyapp.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dhiva.storyapp.data.local.StoryDatabase
import com.dhiva.storyapp.data.remote.ApiService
import com.dhiva.storyapp.data.remote.Resource
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
class SignupViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var storyDatabase: StoryDatabase
    private lateinit var storyRepository: FakeStoryRepository
    private lateinit var signupViewModel: SignupViewModel

    @Before
    fun setUp() {
        storyRepository = FakeStoryRepository(apiService, storyDatabase)
        signupViewModel = SignupViewModel(storyRepository)
    }

    @Test
    fun `when signup should not null and return success`() = runTest {
        storyRepository.state = Resource.Success(0)
        val actualResult = signupViewModel.signup("tes", "tes", "tes").getOrAwaitValue()

        assertNotNull(actualResult)
        assertTrue(actualResult is Resource.Success)
        assertEquals(actualResult.data, DataDummy.generateDummyBasicResponse())
    }

    @Test
    fun `when signup should return loading`() = runTest {
        storyRepository.state = Resource.Loading()
        val actualResult = signupViewModel.signup("tes", "tes", "tes").getOrAwaitValue()

        assertNotNull(actualResult)
        assertTrue(actualResult is Resource.Loading)
    }

    @Test
    fun `when signup should return error`() = runTest {
        storyRepository.state = Resource.Error("Error")
        val actualResult = signupViewModel.signup("tes", "tes", "tes").getOrAwaitValue()

        assertNotNull(actualResult)
        assertTrue(actualResult is Resource.Error)
        assertEquals("Error", actualResult.message)
    }

}