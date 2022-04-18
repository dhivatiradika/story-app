package com.dhiva.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dhiva.storyapp.data.local.StoryDatabase
import com.dhiva.storyapp.data.local.entity.StoryEntity
import com.dhiva.storyapp.data.remote.ApiService
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.BasicResponse
import com.dhiva.storyapp.data.remote.response.ListStoryItem
import com.dhiva.storyapp.model.Story

class StoryRepository(private val apiService: ApiService, private val database: StoryDatabase) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token: String?): LiveData<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }
}