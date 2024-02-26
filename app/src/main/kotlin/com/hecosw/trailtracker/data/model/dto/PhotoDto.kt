package com.hecosw.trailtracker.data.model.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PhotoDto(
    @SerialName("photos") val photos: PhotoList? = null,
    @SerialName("stat") val stat: String? = null
)

@Serializable
data class PhotoList(
    @SerialName("page") val page: Int? = null,
    @SerialName("pages") val pages: Int? = null,
    @SerialName("perpage") val perPage: Int? = null,
    @SerialName("total") val total: Int? = null,
    @SerialName("photo") val photo: List<PhotoItem>? = null
)

@Serializable
data class PhotoItem(
    @SerialName("id") val id: String? = null,
    @SerialName("owner") val owner: String? = null,
    @SerialName("secret") val secret: String? = null,
    @SerialName("server") val server: String? = null,
    @SerialName("farm") val farm: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("ispublic") val isPublic: Int? = null,
    @SerialName("isfriend") val isFriend: Int? = null,
    @SerialName("isfamily") val isFamily: Int? = null,
    @SerialName("url_s") val urlS: String? = null,
    @SerialName("height_s") val heightS: Int? = null,
    @SerialName("width_s") val widthS: Int? = null,
    @SerialName("url_m") val urlM: String? = null,
    @SerialName("height_m") val heightM: Int? = null,
    @SerialName("width_m") val widthM: Int? = null,
    @SerialName("url_n") val urlN: String? = null,
    @SerialName("height_n") val heightN: Int? = null,
    @SerialName("width_n") val widthN: Int? = null
)
