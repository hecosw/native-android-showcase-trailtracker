package com.hecosw.trailtracker.domain.usecase

import arrow.core.Either
import com.hecosw.trailtracker.domain.failure.LocationFailure
import com.hecosw.trailtracker.domain.value.Geolocation
import kotlinx.coroutines.flow.SharedFlow

fun interface SubscribeLocationUpdatesUseCase {
    suspend fun execute(): SharedFlow<Either<LocationFailure, Geolocation>>
}
