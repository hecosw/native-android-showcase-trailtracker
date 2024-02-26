import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import arrow.core.Either
import com.google.android.gms.location.*
import com.hecosw.trailtracker.BuildConfig
import com.hecosw.trailtracker.common.logging.Logger
import com.hecosw.trailtracker.data.source.location.LocationProvider
import com.hecosw.trailtracker.domain.failure.LocationFailure
import com.hecosw.trailtracker.domain.value.Geolocation
import com.hecosw.trailtracker.domain.value.Latitude
import com.hecosw.trailtracker.domain.value.Longitude
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject


class GmsLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val log: Logger,
) : LocationProvider {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val _locationUpdates =
        MutableSharedFlow<Either<LocationFailure, Geolocation>>(replay = 1)

    private val locationUpdates: SharedFlow<Either<LocationFailure, Geolocation>> =
        _locationUpdates.asSharedFlow()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                val geolocation = Geolocation(Latitude(location.latitude), Longitude(location.longitude))
                log.debug("Gms location provider new result: $geolocation")
                val isSuccess = _locationUpdates.tryEmit(Either.Right(geolocation))
                log.debug("    ... emit success: $isSuccess")
            }
        }

        override fun onLocationAvailability(availability: LocationAvailability) {
            super.onLocationAvailability(availability)
            if (!availability.isLocationAvailable) {
                log.debug("Gms location provider new availability: ${availability.isLocationAvailable}")
                val isSuccess = _locationUpdates.tryEmit(Either.Left(LocationFailure.ProviderUnavailable))
                log.debug("    ... emit success: $isSuccess")
            }
        }
    }

    override fun subscribeLocationUpdates(): SharedFlow<Either<LocationFailure, Geolocation>> {
        log.debug("Gms location provider new updates subscriber")
        return locationUpdates
    }

    override fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _locationUpdates.tryEmit(Either.Left(LocationFailure.PermissionDenied))
            log.debug("    ... failed permission check")
            return
        }

        val locationRequest = createLocationRequest()

        log.debug("    ... request updates to fused location client")
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            .addOnFailureListener {
                _locationUpdates.tryEmit(Either.Left(LocationFailure.ProviderUnavailable))
            }
    }

    override fun stopLocationUpdates() {
        log.debug("Gms location provider stop location updates")
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createLocationRequest(): LocationRequest {
        val minimumDistanceMeters = (BuildConfig.MINIMUM_KILOMETERS_FOR_NEW_PHOTO.toFloat() * 1000)
        log.debug("Gms location provider start updates every $minimumDistanceMeters meters")
        return LocationRequest.Builder(0)
            .setMinUpdateDistanceMeters(minimumDistanceMeters)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
    }
}
