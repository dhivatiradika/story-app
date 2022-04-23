package com.dhiva.storyapp.utils

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.dhiva.storyapp.data.local.entity.StoryEntity
import com.dhiva.storyapp.data.remote.response.*
import com.dhiva.storyapp.model.User

object DataDummy {
    fun generateDummyPagingData(): PagingData<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        for (i in 1..5) {
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
        for (i in 1..5) {
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

    fun generateDummyStoryEntity(): List<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        for (i in 1..5) {
            val story = StoryEntity(
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

    fun generateDummyStoriesResponse(): StoriesResponse {
        return StoriesResponse(
            listStory = generateDummyStories(),
            error = false,
            message = "success"
        )
    }

    fun generateDummyLoginResponse(): LoginResponse {
        return LoginResponse(
            loginResult = generateDummyLoginResult(),
            error = false,
            message = "success"

        )
    }

    fun generatePagingSource(): PagingSource<Int, StoryEntity> {
        return PagingSourceUtil()
    }
}