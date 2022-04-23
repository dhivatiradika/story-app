package com.dhiva.storyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.dhiva.storyapp.data.local.StoryDatabase
import com.dhiva.storyapp.data.remote.ApiService
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.fake.FakeApiService
import com.dhiva.storyapp.fake.FakeStoryDao
import com.dhiva.storyapp.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var file: File

    @Mock
    private lateinit var storyDatabase: StoryDatabase
    private lateinit var storyDao: FakeStoryDao
    private lateinit var apiService: ApiService
    private lateinit var storyRepository: StoryRepository

    @Before
    fun setUp() {
        storyDao = FakeStoryDao()
        apiService = FakeApiService()
        storyRepository = StoryRepository(apiService, storyDatabase)
    }

    @Test
    fun `get stories should return not null`() = runTest {
        val expectedResult = storyDao.getAllStory()
        assertNotNull(expectedResult)
    }

    @Test
    fun `getStoriesLocation should emit loading, success with data(not null), and then complete`() =
        runTest {
            storyRepository.getStoriesLocation("").test {
                val emissionLoading = awaitItem()
                val emissionSuccess = awaitItem()
                awaitComplete()

                assertTrue(emissionLoading is Resource.Loading)
                assertTrue(emissionSuccess is Resource.Success)
                assertNotNull(emissionSuccess.data)
            }
        }

    @Test
    fun `login should emit loading, success with data(not null), and then complete`() = runTest {
        launch {
            storyRepository.login("user", "pass").test {
                val emissionLoading = awaitItem()
                val emissionSuccess = awaitItem()
                awaitComplete()

                assertTrue(emissionLoading is Resource.Loading)
                assertTrue(emissionSuccess is Resource.Success)
                assertNotNull(emissionSuccess.data)
            }
        }
    }

    @Test
    fun `signup should emit loading, success with data(not null), and then complete`() = runTest {
        launch {
            storyRepository.signUp("user", "user", "pass").test {
                val emissionLoading = awaitItem()
                val emissionSuccess = awaitItem()
                awaitComplete()

                assertTrue(emissionLoading is Resource.Loading)
                assertTrue(emissionSuccess is Resource.Success)
                assertNotNull(emissionSuccess.data)
            }
        }
    }

    @Test
    fun `uploadStory should emit loading, success with data(not null), and then complete`() =
        runTest {
            launch {
                storyRepository.uploadStory(file, "desc", "token", null).test {
                    val emissionLoading = awaitItem()
                    val emissionSuccess = awaitItem()
                    awaitComplete()

                    assertTrue(emissionLoading is Resource.Loading)
                    assertTrue(emissionSuccess is Resource.Success)
                    assertNotNull(emissionSuccess.data)
                }
            }
        }
}