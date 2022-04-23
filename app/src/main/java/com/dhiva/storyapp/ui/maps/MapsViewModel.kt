package com.dhiva.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhiva.storyapp.data.StoryRepository

class MapsViewModel(private val token: String?, private val storyRepository: StoryRepository) :
    ViewModel() {
    fun stories() = storyRepository.getStoriesLocation(token).asLiveData()
}