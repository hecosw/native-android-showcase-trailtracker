package com.hecosw.trailtracker.ui.screen.trail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hecosw.trailtracker.ui.location.LocationForegroundService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrailActivity : ComponentActivity() {

    private lateinit var viewModel: TrailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel = hiltViewModel()
            TrailComposable(viewModel)
            ViewModelEventHandler(viewModel = viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationService()
    }

    @Composable
    private fun ViewModelEventHandler(viewModel: TrailViewModel) {
        LaunchedEffect(Unit) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.eventFlow.collect { event ->
                        handleViewModelEvent(event)
                    }
                }
            }
        }
    }

    private fun handleViewModelEvent(event: TrailViewModelEvent) {
        when (event) {
            is TrailViewModelEvent.StartLocationService -> {
                startLocationService()
            }
            is TrailViewModelEvent.StopLocationService -> {
                stopLocationService()
            }
        }
    }

    private fun startLocationService() {
        val startIntent = Intent(this, LocationForegroundService::class.java)
        ContextCompat.startForegroundService(this, startIntent)
    }

    private fun stopLocationService() {
        val stopIntent = Intent(this, LocationForegroundService::class.java)
        stopService(stopIntent)
    }
}
