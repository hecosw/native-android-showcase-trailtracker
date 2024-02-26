package com.hecosw.trailtracker.domain.usecase

import arrow.core.Either
import com.hecosw.trailtracker.domain.entity.Photo
import com.hecosw.trailtracker.domain.failure.PhotoFailure
import com.hecosw.trailtracker.domain.value.Geolocation

fun interface GetLocationPhotoUseCase {
    suspend fun execute(location: Geolocation): Either<PhotoFailure, Photo>
}
