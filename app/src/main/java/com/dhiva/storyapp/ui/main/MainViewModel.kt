package com.dhiva.storyapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.dhiva.storyapp.data.remote.ApiConfig
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.StoriesResponse
import com.dhiva.storyapp.model.Story
import com.dhiva.storyapp.model.User
import com.dhiva.storyapp.model.toModel
import com.dhiva.storyapp.utils.AuthPreferences
import com.dhiva.storyapp.utils.preferences
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val prefs = AuthPreferences.getInstance(application.preferences)

    private val _result = MutableLiveData<Resource<List<Story>>>()
    val result: LiveData<Resource<List<Story>>> = _result

    fun getAuthSession(): LiveData<User> {
        _result.value = Resource.Loading()
        return prefs.getUserAuth().asLiveData()
    }

    fun getStories(token: String){
        _result.value = Resource.Loading()
        val authToken = "Bearer $token"
        val client = ApiConfig.getApiService().getStories(authToken)

        client.enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful){
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