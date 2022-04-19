package com.dhiva.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhiva.storyapp.data.StoryRepository

class MapsViewModel(token: String?, storyRepository: StoryRepository): ViewModel() {
    val stories = storyRepository.getStoriesLocation(token).asLiveData()
}