package com.dhiva.storyapp.model

import android.os.Parcelable
import com.dhiva.storyapp.data.local.entity.StoryEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    var photoUrl: String?,
    var createdAt: String?,
    var name: String?,
    var description: String?,
    var lon: Float?,
    var id: String?,
    var lat: Float?
) : Parcelable

fun StoryEntity.toModel(): Story = Story(
    photoUrl = this.photoUrl,
    createdAt = this.createdAt,
    name = this.name,
    description = this.description,
    lon = this.lon,
    id = this.id,
    lat = this.lat
)
