package com.hecosw.trailtracker.domain.usecase

// This is meant to be called from a location foreground service and not
// directly from a view model.  This is for location updates to continue
// even after the user switches off the screen or leaves the app temporarily.

fun interface StartLocationUpdatesUseCase {
    fun execute()
}
