package com.hecosw.trailtracker.data.repository

import arrow.core.Either
import com.hecosw.trailtracker.data.source.location.LocationProvider
import com.hecosw.trailtracker.domain.failure.LocationFailure
import com.hecosw.trailtracker.domain.port.LocationRepository
import com.hecosw.trailtracker.domain.value.Geolocation
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationProvider: LocationProvider
) : LocationRepository {

    override fun subscribeLocationUpdates() : SharedFlow<Either<LocationFailure, Geolocation>> {
        return locationProvider.subscribeLocationUpdates()
    }

    override fun startLocationUpdates() {
        locationProvider.startLocationUpdates()
    }

    override fun stopLocationUpdates() {
        locationProvider.stopLocationUpdates()
    }

}
