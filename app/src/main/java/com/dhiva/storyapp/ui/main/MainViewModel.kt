package com.dhiva.storyapp.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dhiva.storyapp.data.StoryRepository
import com.dhiva.storyapp.data.remote.ApiConfig
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.ListStoryItem
import com.dhiva.storyapp.data.remote.response.StoriesResponse
import com.dhiva.storyapp.model.Story
import com.dhiva.storyapp.model.toModel
import com.dhiva.storyapp.utils.AuthPreferences
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(storyRepository: StoryRepository, private val token: String?, private val prefs: AuthPreferences) : ViewModel() {

    private val _result = MutableLiveData<Resource<List<Story>>>()
    val result: LiveData<Resource<List<Story>>> = _result

    val stories: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories(token).cachedIn(viewModelScope)

    fun logout() = viewModelScope.launch {
        prefs.removeUserAuth()
    }

    fun getStories() {
        _result.value = Resource.Loading()
        val authToken = "Bearer $token"
        val client = ApiConfig.getApiService().getStories(authToken)

        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.listStory?.run {
                        _result.value = Resource.Success(this.toModel())
                    }
                } else {
                    response.errorBody()?.run {
                        val message = JSONObject(this.string()).getString("message")
                        _result.value = Resource.Error(message)
                    }
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _result.value = Resource.Error(t.message.toString())
            }

        })
    }
}