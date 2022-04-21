package com.dhiva.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dhiva.storyapp.data.local.StoryDatabase
import com.dhiva.storyapp.data.remote.ApiService
import com.dhiva.storyapp.fake.FakeStoryRepository
import com.dhiva.storyapp.utils.AuthPreferences
import com.dhiva.storyapp.utils.MainCoroutineRule
import com.dhiva.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var authPreferences: AuthPreferences
    @Mock
    private lateinit var apiService: ApiService
    @Mock
    private lateinit var storyDatabase: StoryDatabase
    private lateinit var storyRepository: FakeStoryRepository
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        storyRepository = FakeStoryRepository(apiService, storyDatabase)
        mainViewModel = MainViewModel(storyRepository, "", authPreferences)
    }

    @InternalCoroutinesApi
    @Test
    fun `get stories return not null`()  {
        val actualStories = mainViewModel.stories().getOrAwaitValue()
        assertNotNull(actualStories)
    }

    @Test
    fun `remove user auth should called when logout`() = runTest {
        mainViewModel.logout()
        Mockito.verify(authPreferences).removeUserAuth()
    }
}