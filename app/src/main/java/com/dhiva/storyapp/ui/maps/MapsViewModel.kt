package com.dhiva.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dhiva.storyapp.data.remote.RemoteDataSource
import kotlinx.coroutines.launch

class MapsViewModel(token: String?, remoteDataSource: RemoteDataSource): ViewModel() {
    val stories = remoteDataSource.getStoriesLocation(token).asLiveData()
}