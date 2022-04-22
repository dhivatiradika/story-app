package com.dhiva.storyapp.fake

import androidx.paging.PagingSource
import androidx.room.paging.LimitOffsetPagingSource
import com.dhiva.storyapp.data.local.dao.StoryDao
import com.dhiva.storyapp.data.local.entity.StoryEntity
import com.dhiva.storyapp.utils.DataDummy
import com.dhiva.storyapp.utils.PagingSourceUtil

class FakeStoryDao : StoryDao {

    private var storyList = mutableListOf<StoryEntity>()

    override suspend fun insertStory(stories: List<StoryEntity>) {
        storyList.addAll(stories)
    }

    override fun getAllStory(): PagingSource<Int, StoryEntity> {
        return DataDummy.generatePagingSource()
    }

    override suspend fun deleteAll() {
        storyList.clear()
    }

}