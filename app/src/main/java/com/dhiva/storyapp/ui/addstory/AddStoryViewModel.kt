package com.dhiva.storyapp.ui.addstory

import androidx.lifecycle.*
import com.dhiva.storyapp.data.remote.ApiConfig
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.BasicResponse
import com.dhiva.storyapp.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryViewModel(private val token: String?) : ViewModel() {
    private val _result = MutableLiveData<Resource<BasicResponse>>()
    val result: LiveData<Resource<BasicResponse>> = _result

    fun uploadImage(getFile: File?, desc: String) {
        val file = reduceFileImage(getFile as File)

        val authToken = "Bearer $token"
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val client = ApiConfig.getApiService().uploadImage(
            token = authToken,
            file = imageMultipart,
            description = description
        )

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