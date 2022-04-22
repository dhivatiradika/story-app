package com.dhiva.storyapp.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dhiva.storyapp.data.local.entity.StoryEntity

class PagingSourceUtil : PagingSource<Int, StoryEntity>() {
    override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
        return LoadResult.Page(
            data = DataDummy.generateDummyStoryEntity(),
            prevKey = 0,
            nextKey = 1
        )
    }
}