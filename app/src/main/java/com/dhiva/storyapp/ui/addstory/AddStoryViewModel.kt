package com.dhiva.storyapp.ui.addstory

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhiva.storyapp.data.StoryRepository
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.BasicResponse
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository, private val token: String?) :
    ViewModel() {

    fun uploadStory(
        getFile: File?,
        desc: String,
        location: Location?
    ): LiveData<Resource<BasicResponse>> =
        storyRepository.uploadStory(getFile, desc, token, location).asLiveData()
}