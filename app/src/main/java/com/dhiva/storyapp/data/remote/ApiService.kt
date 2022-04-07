package com.dhiva.storyapp.data.remote

import com.dhiva.storyapp.data.remote.response.BasicResponse
import com.dhiva.storyapp.data.remote.response.LoginResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(
        @Body requestBody: RequestBody
    ): Call<LoginResponse>

    @POST("register")
    fun signup(
        @Body requestBody: RequestBody
    ): Call<BasicResponse>
}