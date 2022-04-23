package com.dhiva.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dhiva.storyapp.data.StoryRepository
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.LoginResult
import com.dhiva.storyapp.model.User
import com.dhiva.storyapp.utils.AuthPreferences
import kotlinx.coroutines.launch

class LoginViewModel(
    private val storyRepository: StoryRepository,
    private val prefs: AuthPreferences
) : ViewModel() {

    fun login(email: String, password: String): LiveData<Resource<LoginResult>> =
        storyRepository.login(email, password).asLiveData()

    fun getAuthSession(): LiveData<User> = prefs.getUserAuth().asLiveData()

    fun setAuthSession(user: User) = viewModelScope.launch {
        prefs.saveUserAuth(user)
    }

}