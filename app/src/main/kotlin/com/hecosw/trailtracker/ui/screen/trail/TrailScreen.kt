package com.hecosw.trailtracker.ui.screen.trail

import android.os.Build
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.hecosw.trailtracker.R
import com.hecosw.trailtracker.domain.entity.Photo
import com.hecosw.trailtracker.ui.theme.TrailTrackerTheme
import com.hecosw.trailtracker.ui.test.TestSet

data class TrailComposableEventHandlers(
    val onStartOrStopButtonClicked: () -> Unit = {},
    val onLocationPermissionRationaleConfirmed: () -> Unit = {},
    val onLocationPermissionRationaleDismissed: () -> Unit = {},
    val onLocationPermissionGranted: () -> Unit = {},
    val onLocationPermissionDenied: () -> Unit = {},
    val onSystemLocationEnablingRequestDialogDismissed: () -> Unit = {},
    val onNetworkErrorDialogDismissed: () -> Unit = {},
)

@Composable
fun TrailComposable(viewModel: TrailViewModel = hiltViewModel()) {
    TrailComposable(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        eventHandlers = TrailComposableEventHandlers(
            onStartOrStopButtonClicked = viewModel::onStartOrStopButtonClicked,
            onLocationPermissionRationaleConfirmed = viewModel::onLocationPermissionRationaleConfirmed,
            onLocationPermissionRationaleDismissed = viewModel::onLocationPermissionRationaleDismissed,
            onLocationPermissionGranted = viewModel::onLocationPermissionGranted,
            onLocationPermissionDenied = viewModel::onLocationPermissionDenied,
            onSystemLocationEnablingRequestDialogDismissed = viewModel::onSystemLocationEnablingRequestDialogDismissed,
            onNetworkErrorDialogDismissed = viewModel::onNetworkErrorDialogDismissed,
        )
    )
}

@Composable
fun TrailComposable(
    state: TrailViewModelState = TrailViewModelState(),
    eventHandlers: TrailComposableEventHandlers = TrailComposableEventHandlers(),
) {
    TrailScreenContent(
        photoList = state.photoList,
        isOngoing = state.isOngoing,
        eventHandlers = eventHandlers
    )

    ConditionalLocationPermissionRationaleDialog(
        overlay = state.overlay,
        onConfirmed = eventHandlers.onLocationPermissionRationaleConfirmed,
        onDismissed = eventHandlers.onLocationPermissionRationaleDismissed
    )

    ConditionalLocationPermissionSystemRequest(
        overlay = state.overlay,
        onGranted = eventHandlers.onLocationPermissionGranted,
        onDenied = eventHandlers.onLocationPermissionDenied
    )

    ConditionalSystemLocationEnablingRequestDialog(
        overlay = state.overlay,
        onDismissed = eventHandlers.onSystemLocationEnablingRequestDialogDismissed
    )

    ConditionalNetworkErrorDialog(
        overlay = state.overlay,
        onDismissed = eventHandlers.onNetworkErrorDialogDismissed
    )
}

@Composable
private fun ConditionalNetworkErrorDialog(
    overlay: TrailViewModelOverlay,
    onDismissed: () -> Unit,
) {
    if (overlay == TrailViewModelOverlay.NetworkErrorDialog) {
        NetworkErrorDialog(
            onDismissed = onDismissed
        )
    }
}

@Composable
private fun ConditionalSystemLocationEnablingRequestDialog(
    overlay: TrailViewModelOverlay,
    onDismissed: () -> Unit,
) {
    if (overlay == TrailViewModelOverlay.SystemLocationEnablingRequestDialog) {
        SystemLocationEnablingRequestDialog(
            onDismissed = onDismissed
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ConditionalLocationPermissionSystemRequest(
    overlay: TrailViewModelOverlay,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    if (overlay == TrailViewModelOverlay.LocationPermissionSystemRequest) {
        val locationPermissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                if (Build.VERSION.SDK_INT >= 33) android.Manifest.permission.POST_NOTIFICATIONS else ""
            ),
            onPermissionsResult = { permissionMap ->
                if (permissionMap.any { !it.value }) {
                    onDenied()
                } else {
                    onGranted()
                }
            }
        )
        DisposableEffect(Unit) {
            locationPermissionsState.launchMultiplePermissionRequest()
            onDispose { }
        }
    }
}

@Composable
private fun ConditionalLocationPermissionRationaleDialog(
    overlay: TrailViewModelOverlay,
    onConfirmed: () -> Unit,
    onDismissed: () -> Unit
) {
    if (overlay == TrailViewModelOverlay.LocationPermissionRationaleDialog) {
        LocationPermissionRationaleDialog(
            onConfirmed = onConfirmed,
            onDismissed = onDismissed
        )
    }
}

@Composable
private fun TrailScreenContent(
    photoList: List<Photo>,
    isOngoing: Boolean,
    eventHandlers: TrailComposableEventHandlers,
) {
    TrailTrackerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val startLabel = stringResource(id = R.string.generic_start).uppercase()
                val stopLabel = stringResource(id = R.string.generic_stop).uppercase()
                Text(
                    text = if (isOngoing) stopLabel else startLabel,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isOngoing) {
                        MaterialTheme.colorScheme.onSecondary
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(20.dp)
                        .clickable { eventHandlers.onStartOrStopButtonClicked() }
                )
                HorizontalDivider(modifier = Modifier.shadow(elevation = 3.dp))
                PhotoGallery(photoList)
            }
        }
    }
}

@Composable
fun PhotoGallery(photos: List<Photo>) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items = photos, key = { photo -> photo.id.value }) {
            photo -> TrailPhoto(photo)
        }
    }
}

@Composable
fun TrailPhoto(photo: Photo) {
    AsyncImage(
        model = photo.url.value,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        contentScale = ContentScale.Crop,
        contentDescription = "Title: ${photo.title.value}"
    )
}

@Preview(showBackground = true, backgroundColor = 0xff000000, widthDp = 300)
@Composable
fun PreviewMainScreen() {
    TrailComposable(
        state = TrailViewModelState(photoList = TestSet.set01, isOngoing = true)
    )
}
