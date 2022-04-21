package com.dhiva.storyapp.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dhiva.storyapp.data.local.entity.StoryEntity
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.BasicResponse
import com.dhiva.storyapp.data.remote.response.ListStoryItem
import com.dhiva.storyapp.data.remote.response.LoginResult
import com.dhiva.storyapp.model.User
import kotlinx.coroutines.flow.Flow

object DataDummy {
    fun generateDummyPagingData(): PagingData<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        for (i in 1..5){
            val story = StoryEntity(
                id = "$i",
                photoUrl = "www.test.com/img.jpg",
                createdAt = "2022-04-20T16:03:17.000Z",
                name = "test $i",
                description = "desc",
            )
            storyList.add(story)
        }
        return PagingData.from(storyList)
    }

    fun generateDummyLoginResult(): LoginResult =
        LoginResult(
            name = "tes",
            userId = "123",
            token = "123"
        )

    fun generateDummyUser(): User =
        User(
            name = "tes",
            userId = "123",
            token = "123"
        )

    fun generateDummyBasicResponse(): BasicResponse =
        BasicResponse(
            error = false,
            message = "success"
        )

    fun generateDummyStories(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 1..5){
            val story = ListStoryItem(
                id = "$i",
                photoUrl = "www.test.com/img.jpg",
                createdAt = "2022-04-20T16:03:17.000Z",
                name = "test $i",
                description = "desc",
            )
            storyList.add(story)
        }
        return storyList
    }
}