package com.hecosw.trailtracker.domain.port

import arrow.core.Either
import com.hecosw.trailtracker.domain.failure.LocationFailure
import com.hecosw.trailtracker.domain.value.Geolocation
import kotlinx.coroutines.flow.SharedFlow

interface LocationRepository {
    fun subscribeLocationUpdates() : SharedFlow<Either<LocationFailure, Geolocation>>
    fun startLocationUpdates()
    fun stopLocationUpdates()
}
