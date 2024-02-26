package com.hecosw.trailtracker.domain.interactor

import com.hecosw.trailtracker.domain.port.LocationRepository
import com.hecosw.trailtracker.domain.usecase.StartLocationUpdatesUseCase
import javax.inject.Inject

class StartLocationUpdatesInteractor @Inject constructor(
    private val locationRepository: LocationRepository
) : StartLocationUpdatesUseCase {

    override fun execute() {
        locationRepository.startLocationUpdates()
    }
}
