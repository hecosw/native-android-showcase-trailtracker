package com.hecosw.trailtracker.ui.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationPermissionCheckerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationPermissionChecker {

    override fun check(): Boolean {
        return checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun checkPermission(name: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, name) == PackageManager.PERMISSION_GRANTED
    }

}
