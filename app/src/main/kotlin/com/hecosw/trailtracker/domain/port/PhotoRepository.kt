package com.hecosw.trailtracker.domain.port

import arrow.core.Either
import com.hecosw.trailtracker.domain.entity.Photo
import com.hecosw.trailtracker.domain.failure.PhotoFailure
import com.hecosw.trailtracker.domain.value.Geolocation

fun interface PhotoRepository {
    suspend fun getLocationPhoto(location: Geolocation): Either<PhotoFailure, Photo>
}
