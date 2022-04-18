package com.dhiva.storyapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dhiva.storyapp.data.remote.response.ListStoryItem
import com.dhiva.storyapp.model.Story

@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey
    val id: String,
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lon: Float? = null,
    val lat: Float? = null
)

fun List<ListStoryItem>.toEntity() : List<StoryEntity>{
    return this.map {
        StoryEntity(
            photoUrl = it.photoUrl,
            createdAt = it.createdAt,
            name = it.name,
            description = it.description,
            lon = it.lon,
            id = it.id,
            lat = it.lat
        )
    }
}