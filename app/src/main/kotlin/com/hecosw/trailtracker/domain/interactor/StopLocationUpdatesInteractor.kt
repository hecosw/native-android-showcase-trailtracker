package com.hecosw.trailtracker.domain.interactor

import com.hecosw.trailtracker.domain.port.LocationRepository
import com.hecosw.trailtracker.domain.usecase.StopLocationUpdatesUseCase
import javax.inject.Inject

class StopLocationUpdatesInteractor @Inject constructor(
    private val locationRepository: LocationRepository
) : StopLocationUpdatesUseCase {

    override fun execute() {
        locationRepository.stopLocationUpdates()
    }
}
