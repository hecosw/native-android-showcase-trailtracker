package com.hecosw.trailtracker.domain.failure

sealed class PhotoFailure {
    data object PhotoUnavailable : PhotoFailure()
    data object RequestError : PhotoFailure()
    data object ResponseError : PhotoFailure()
    data object UnknownError : PhotoFailure()
}
