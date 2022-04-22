package com.dhiva.storyapp.fake

import com.dhiva.storyapp.data.remote.ApiService
import com.dhiva.storyapp.data.remote.response.BasicResponse
import com.dhiva.storyapp.data.remote.response.LoginResponse
import com.dhiva.storyapp.data.remote.response.StoriesResponse
import com.dhiva.storyapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    override suspend fun login(requestBody: RequestBody): LoginResponse {
        return DataDummy.generateDummyLoginResponse()
    }

    override suspend fun signup(requestBody: RequestBody): BasicResponse {
        return DataDummy.generateDummyBasicResponse()
    }

    override suspend fun getStories(
        token: String,
        page: Int,
        size: Int,
        location: Int?
    ): StoriesResponse {
        return DataDummy.generateDummyStoriesResponse()
    }

    override suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?
    ): BasicResponse {
        return DataDummy.generateDummyBasicResponse()
    }
}