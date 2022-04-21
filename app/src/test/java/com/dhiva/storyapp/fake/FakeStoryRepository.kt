package com.dhiva.storyapp.fake

import android.location.Location
import androidx.paging.PagingData
import com.dhiva.storyapp.data.StoryRepository
import com.dhiva.storyapp.data.local.StoryDatabase
import com.dhiva.storyapp.data.local.entity.StoryEntity
import com.dhiva.storyapp.data.remote.ApiService
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.BasicResponse
import com.dhiva.storyapp.data.remote.response.ListStoryItem
import com.dhiva.storyapp.data.remote.response.LoginResult
import com.dhiva.storyapp.utils.DataDummy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.File

class FakeStoryRepository(apiService: ApiService, database: StoryDatabase) :
    StoryRepository(apiService, database) {
    var state: Resource<Int> = Resource.Loading()

    override fun getStories(token: String?): Flow<PagingData<StoryEntity>> {
        return flowOf(
            DataDummy.generateDummyPagingData()
        )
    }

    override fun login(email: String, password: String): Flow<Resource<LoginResult>> {
        return when (state) {
            is Resource.Success -> flowOf(Resource.Success(DataDummy.generateDummyLoginResult()))
            is Resource.Loading -> flowOf(Resource.Loading())
            is Resource.Error -> flowOf(Resource.Error("Error"))
            else -> flowOf(Resource.Error("Error"))
        }
    }

    override fun signUp(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<BasicResponse>> {
        return when (state) {
            is Resource.Success -> flowOf(Resource.Success(DataDummy.generateDummyBasicResponse()))
            is Resource.Loading -> flowOf(Resource.Loading())
            is Resource.Error -> flowOf(Resource.Error("Error"))
            else -> flowOf(Resource.Error("Error"))
        }
    }

    override fun uploadStory(
        getFile: File?,
        desc: String,
        token: String?,
        location: Location?
    ): Flow<Resource<BasicResponse>> {
        return when (state) {
            is Resource.Success -> flowOf(Resource.Success(DataDummy.generateDummyBasicResponse()))
            is Resource.Loading -> flowOf(Resource.Loading())
            is Resource.Error -> flowOf(Resource.Error("Error"))
            else -> flowOf(Resource.Error("Error"))
        }
    }

    override fun getStoriesLocation(token: String?): Flow<Resource<List<ListStoryItem>>> {
        return when (state) {
            is Resource.Success -> flowOf(Resource.Success(DataDummy.generateDummyStories()))
            is Resource.Loading -> flowOf(Resource.Loading())
            is Resource.Error -> flowOf(Resource.Error("Error"))
            else -> flowOf(Resource.Error("Error"))
        }
    }
}