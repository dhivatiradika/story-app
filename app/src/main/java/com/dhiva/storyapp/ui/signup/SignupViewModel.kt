package com.dhiva.storyapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhiva.storyapp.data.remote.ApiConfig
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.BasicResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {
    private val _result = MutableLiveData<Resource<BasicResponse>>()
    val result: LiveData<Resource<BasicResponse>> = _result

    fun signup(name: String, email: String, password: String) {
        _result.value = Resource.Loading()
        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("email", email)
        jsonObject.put("password", password)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val client = ApiConfig.getApiService().signup(requestBody)
        client.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    response.body()?.run {
                        _result.value = Resource.Success(this)
                    }
                } else {
                    response.errorBody()?.run {
                        val message = JSONObject(this.string()).getString("message")
                        _result.value = Resource.Error(message)
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                _result.value = Resource.Error(t.message.toString())
            }

        })
    }
}