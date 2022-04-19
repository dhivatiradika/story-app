package com.dhiva.storyapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhiva.storyapp.data.StoryRepository
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.BasicResponse

class SignupViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun signup(name: String, email: String, password: String): LiveData<Resource<BasicResponse>> =
        storyRepository.signUp(name, email, password).asLiveData()

}