package com.hecosw.trailtracker.domain.entity

import com.hecosw.trailtracker.domain.value.PhotoId
import com.hecosw.trailtracker.domain.value.PhotoTitle
import com.hecosw.trailtracker.domain.value.PhotoUrl
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    val id: PhotoId = PhotoId(0),
    val title: PhotoTitle = PhotoTitle(""),
    val url: PhotoUrl = PhotoUrl("")
)
