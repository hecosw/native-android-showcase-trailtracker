package com.hecosw.trailtracker.domain.interactor

import arrow.core.Either
import com.hecosw.trailtracker.domain.entity.Photo
import com.hecosw.trailtracker.domain.failure.PhotoFailure
import com.hecosw.trailtracker.domain.port.PhotoRepository
import com.hecosw.trailtracker.domain.usecase.GetLocationPhotoUseCase
import com.hecosw.trailtracker.domain.value.Geolocation
import javax.inject.Inject

class GetLocationPhotoInteractor @Inject constructor(
    private val photoRepository: PhotoRepository
) : GetLocationPhotoUseCase {

    override suspend fun execute(location: Geolocation): Either<PhotoFailure, Photo> {
        return photoRepository.getLocationPhoto(location)
    }
}
