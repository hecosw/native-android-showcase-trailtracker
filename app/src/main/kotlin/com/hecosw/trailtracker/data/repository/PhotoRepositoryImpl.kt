package com.hecosw.trailtracker.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.hecosw.trailtracker.common.logging.Logger
import com.hecosw.trailtracker.data.model.dto.PhotoDto
import com.hecosw.trailtracker.data.model.mapper.PhotoDtoMapper
import com.hecosw.trailtracker.data.source.network.PhotoApi
import com.hecosw.trailtracker.domain.entity.Photo
import com.hecosw.trailtracker.domain.failure.PhotoFailure
import com.hecosw.trailtracker.domain.port.PhotoRepository
import com.hecosw.trailtracker.domain.value.Geolocation
import com.skydoves.sandwich.getOrElse
import com.skydoves.sandwich.ktor.statusCode
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoApi: PhotoApi,
    private val photoDtoMapper: PhotoDtoMapper,
    private val log: Logger
) : PhotoRepository {

    override suspend fun getLocationPhoto(location: Geolocation): Either<PhotoFailure, Photo> {
        val response = photoApi.getLocationPhoto(location)
        log.debug("Photo repo get location photo response: $response")
        var result: Either<PhotoFailure, Photo> = PhotoFailure.UnknownError.left()
        response.onSuccess {
            val photo = response.getOrElse(PhotoDto()).photos?.photo ?: emptyList()
            result = if (photo.isEmpty()) {
                PhotoFailure.PhotoUnavailable.left()
            } else {
                photoDtoMapper.map(data).right()
            }
        }.onError {
            result = when (statusCode.code) {
                in 400..499 -> PhotoFailure.RequestError.left()
                in 500..599 -> PhotoFailure.ResponseError.left()
                else -> PhotoFailure.UnknownError.left()
            }
        }
        log.debug("    ... $result")
        return result
    }

}
