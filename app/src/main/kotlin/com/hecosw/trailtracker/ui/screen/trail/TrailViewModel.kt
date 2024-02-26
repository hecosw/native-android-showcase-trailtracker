package com.hecosw.trailtracker.ui.screen.trail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hecosw.trailtracker.common.logging.Logger
import com.hecosw.trailtracker.di.DispatcherModule
import com.hecosw.trailtracker.domain.entity.Photo
import com.hecosw.trailtracker.domain.failure.LocationFailure
import com.hecosw.trailtracker.domain.failure.PhotoFailure
import com.hecosw.trailtracker.domain.usecase.GetLocationPhotoUseCase
import com.hecosw.trailtracker.domain.usecase.SubscribeLocationUpdatesUseCase
import com.hecosw.trailtracker.domain.value.Geolocation
import com.hecosw.trailtracker.domain.value.PhotoId
import com.hecosw.trailtracker.ui.location.LocationPermissionChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject

sealed class TrailViewModelEvent {
    data object StartLocationService : TrailViewModelEvent()
    data object StopLocationService : TrailViewModelEvent()
}

@Serializable
sealed class TrailViewModelOverlay {
    @Serializable data object None : TrailViewModelOverlay()
    @Serializable data object NetworkErrorDialog : TrailViewModelOverlay()
    @Serializable data object LocationPermissionSystemRequest : TrailViewModelOverlay()
    @Serializable data object LocationPermissionRationaleDialog : TrailViewModelOverlay()
    @Serializable data object SystemLocationEnablingRequestDialog : TrailViewModelOverlay()
}

@Serializable
data class TrailViewModelState(
    val isOngoing: Boolean = false,
    val photoList: List<Photo> = emptyList(),
    val overlay: TrailViewModelOverlay = TrailViewModelOverlay.None
)

@HiltViewModel
class TrailViewModel @Inject constructor(
    private val log: Logger,
    private val savedStateHandle: SavedStateHandle,
    private val locationPermissionChecker: LocationPermissionChecker,
    private val subscribeLocationUpdatesUseCase: SubscribeLocationUpdatesUseCase,
    private val getLocationPhotoUseCase: GetLocationPhotoUseCase,
    @DispatcherModule.IODispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<TrailViewModelEvent>()
    val eventFlow: Flow<TrailViewModelEvent> = _eventFlow

    private val _state = MutableStateFlow(
        savedStateHandle.get<String>(TRAIL_SAVED_STATE_KEY)?.toTrailViewModelState()
            ?: TrailViewModelState()
    )
    val state = _state.asStateFlow()

    private var locationUpdatesJob: Job? = null

    override fun onCleared() {
        locationUpdatesJob?.cancel()
        super.onCleared()
    }

    fun onStartOrStopButtonClicked() {
        viewModelScope.launch {
            if (state.value.isOngoing) {
                stopTrailSession()
            } else {
                startTrailSession()
            }
            saveState()
        }
    }

    fun onNetworkErrorDialogDismissed() {
        viewModelScope.launch {
            log.info("Network error dialog dismissed")
            hideOverlays()
        }
    }

    fun onLocationPermissionRationaleDismissed() {
        viewModelScope.launch {
            log.info("Location permission rationale dismissed")
            hideOverlays()
        }
    }

    fun onLocationPermissionRationaleConfirmed() {
        viewModelScope.launch {
            log.info("Location permission rationale confirmed")
            overlay(TrailViewModelOverlay.LocationPermissionSystemRequest)
        }
    }

    fun onLocationPermissionGranted() {
        viewModelScope.launch {
            log.info("Location permission granted")
            hideOverlays()
            startTrailSession()
        }
    }

    fun onLocationPermissionDenied() {
        viewModelScope.launch {
            log.info("Location permission denied")
            hideOverlays()
        }
    }

    fun onSystemLocationEnablingRequestDialogDismissed() {
        viewModelScope.launch {
            log.info("System location enabling request dialog dismissed")
            hideOverlays()
        }
    }

    private suspend fun stopTrailSession() {
        log.info("Stop trail session")
        locationUpdatesJob?.cancel()
        locationUpdatesJob = null
        if (state.value.isOngoing) {
            _eventFlow.emit(TrailViewModelEvent.StopLocationService)
            _state.amend { copy(isOngoing = false) }
        } else {
            log.info("    ... no ongoing session")
        }
    }

    private fun startTrailSession() {
        log.info("Start trail session")
        if (!locationPermissionChecker.check()) {
            log.info("    ... permisson check failed, show rationale dialog")
            overlay(TrailViewModelOverlay.LocationPermissionRationaleDialog)
        } else {
            log.info("    ... permisson check passed")
            _state.amend { copy(photoList = emptyList(), isOngoing = true) }
            launchLocationUpdateSubscription()
        }
    }

    private fun launchLocationUpdateSubscription() {
        locationUpdatesJob = viewModelScope.launch(ioDispatcher) {
            log.info("Start location updates subscription")
            _eventFlow.emit(TrailViewModelEvent.StartLocationService)
            subscribeLocationUpdatesUseCase.execute().collect { eitherResult ->
                eitherResult.fold(
                    { failure -> handleLocationFailure(failure) },
                    { geolocation -> handleLocationUpdate(geolocation) }
                )
            }
        }
    }

    private fun handleLocationUpdate(geolocation: Geolocation) {
        log.info("New location update")
        log.debug("    ... $geolocation")
        hideOverlays()
        fetchLocationPhoto(geolocation)
    }

    private fun overlay(type: TrailViewModelOverlay) {
        _state.amend { copy(overlay = type) }
    }

    private fun hideOverlays() {
        log.debug("Hide all dialogs and overlays")
        overlay(TrailViewModelOverlay.None)
    }

    private fun handleLocationFailure(failure: LocationFailure) {
        when (failure) {
            is LocationFailure.PermissionDenied -> {
                log.info("New update: location permission denied")
                overlay(TrailViewModelOverlay.LocationPermissionRationaleDialog)
            }
            is LocationFailure.ProviderUnavailable -> {
                log.info("New update: location provider unavailable")
                overlay(TrailViewModelOverlay.SystemLocationEnablingRequestDialog)
            }
        }
    }

    private fun fetchLocationPhoto(geolocation: Geolocation) {
        viewModelScope.launch(ioDispatcher) {
            getLocationPhotoUseCase.execute(geolocation)
                .fold(
                    { photoFailure -> handlePhotoFailure(photoFailure) },
                    { photo -> handleNewPhoto(photo) }
                )
        }
    }

    private fun handlePhotoFailure(failure: PhotoFailure) {
        log.warning("Photo failure: $failure")
        if (failure is PhotoFailure.UnknownError) {
            overlay(TrailViewModelOverlay.NetworkErrorDialog)
        }
    }

    private fun handleNewPhoto(photo: Photo) {
        log.info("Handle new photo")
        log.debug("    ... $photo")
        val list = state.value.photoList
        val last = if (list.isEmpty()) Photo() else list.first()
        if (photo.url != last.url) {
            val updatedList = state.value.photoList.prepended(photo.withTimestampId())
            _state.amend { copy(photoList = updatedList) }
            saveState()
        } else {
            log.debug("    ... not adding consecutive redundant image")
        }
    }

    private fun <T> List<T>.prepended(item: T) = listOf(item) + this

    private fun Photo.withTimestampId(): Photo {
        // Photos are never repeated consecutively, but if the user travels
        // a little further, gets a new photo, then returns, it is possible
        // and likely that the cloud will return the same photo, which has
        // the same id that is already present in the photo list.
        // That would result in a crash of the LazyColumn that is being
        // optimized with a unique key for every list item.  Therefore, use
        // the current timestamp to guarantee the uniqueness of the key.
        return this.copy(id = PhotoId(System.currentTimeMillis()))
    }

    private fun <T> MutableStateFlow<T>.amend(block: T.() -> T) {
        val currentState = this.value
        this.update { block(currentState) }
    }

    private fun TrailViewModelState.toJson(): String {
        return Json.encodeToString(TrailViewModelState.serializer(), this)
    }

    private fun String.toTrailViewModelState(): TrailViewModelState {
        return Json.decodeFromString(this)
    }

    private fun saveState() {
        val stateJson = state.value.toJson()
        savedStateHandle["TRAIL_SAVED_STATE_KEY"] = stateJson
    }

    private companion object {
        const val TRAIL_SAVED_STATE_KEY = "trail_state"
    }

}
