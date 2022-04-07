package com.dhiva.storyapp.ui.login

import android.app.Application
import androidx.lifecycle.*
import com.dhiva.storyapp.data.remote.ApiConfig
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.LoginResponse
import com.dhiva.storyapp.data.remote.response.LoginResult
import com.dhiva.storyapp.model.User
import com.dhiva.storyapp.utils.AuthPreferences
import com.dhiva.storyapp.utils.preferences
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val prefs = AuthPreferences.getInstance(application.preferences)

    private val _result = MutableLiveData<Resource<LoginResult>>()
    val result: LiveData<Resource<LoginResult>> = _result

    fun login(email: String, password: String){
        _result.value = Resource.Loading()
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val client = ApiConfig.getApiService().login(requestBody)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                   response.body()?.loginResult?.run {
                       _result.value = Resource.Success(this)
                   }
                } else {
                    response.errorBody()?.run {
                        val message = JSONObject(this.string()).getString("message")
                        _result.value = Resource.Error(message)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _result.value = Resource.Error(t.message.toString())
            }

        })
    }

    fun getAuthSession(): LiveData<User> = prefs.getUserAuth().asLiveData()

    fun setAuthSession(user: User) = viewModelScope.launch {
        prefs.saveUserAuth(user)
    }

}