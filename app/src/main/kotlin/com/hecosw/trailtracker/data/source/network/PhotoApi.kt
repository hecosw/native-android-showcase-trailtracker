package com.hecosw.trailtracker.data.source.network

import com.hecosw.trailtracker.data.model.dto.PhotoDto
import com.hecosw.trailtracker.domain.value.Geolocation
import com.skydoves.sandwich.ApiResponse

fun interface PhotoApi {
    suspend fun getLocationPhoto(location: Geolocation): ApiResponse<PhotoDto>
}
