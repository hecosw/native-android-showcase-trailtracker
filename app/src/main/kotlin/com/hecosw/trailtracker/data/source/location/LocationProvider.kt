package com.hecosw.trailtracker.data.source.location

import arrow.core.Either
import com.hecosw.trailtracker.domain.failure.LocationFailure
import com.hecosw.trailtracker.domain.value.Geolocation
import kotlinx.coroutines.flow.SharedFlow

interface LocationProvider {
    fun subscribeLocationUpdates(): SharedFlow<Either<LocationFailure, Geolocation>>
    fun startLocationUpdates()
    fun stopLocationUpdates()
}
