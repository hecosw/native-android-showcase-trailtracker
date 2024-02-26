package com.hecosw.trailtracker.domain.failure

sealed class LocationFailure {
    data object PermissionDenied : LocationFailure()
    data object ProviderUnavailable : LocationFailure()
}
