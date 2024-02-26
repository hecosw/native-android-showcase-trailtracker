package com.hecosw.trailtracker.domain.interactor

import arrow.core.Either
import com.hecosw.trailtracker.domain.failure.LocationFailure
import com.hecosw.trailtracker.domain.port.LocationRepository
import com.hecosw.trailtracker.domain.usecase.SubscribeLocationUpdatesUseCase
import com.hecosw.trailtracker.domain.value.Geolocation
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class SubscribeLocationUpdatesInteractor @Inject constructor(
    private val locationRepository: LocationRepository
) : SubscribeLocationUpdatesUseCase {

    override suspend fun execute(): SharedFlow<Either<LocationFailure, Geolocation>> {
        return locationRepository.subscribeLocationUpdates()
    }
}
