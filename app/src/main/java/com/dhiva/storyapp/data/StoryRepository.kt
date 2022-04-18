package com.dhiva.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dhiva.storyapp.data.remote.ApiService
import com.dhiva.storyapp.data.remote.response.ListStoryItem

class StoryRepository(private val apiService: ApiService) {
    fun getStories(token: String?): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(token, apiService)
            }
        ).liveData
    }
}