package com.dhiva.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dhiva.storyapp.data.StoryRepository
import com.dhiva.storyapp.data.local.entity.StoryEntity
import com.dhiva.storyapp.utils.AuthPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    storyRepository: StoryRepository,
    token: String?,
    private val prefs: AuthPreferences
) : ViewModel() {
    val stories: LiveData<PagingData<StoryEntity>> =
        storyRepository.getStories(token).cachedIn(viewModelScope)

    fun logout() = viewModelScope.launch {
        prefs.removeUserAuth()
    }
}