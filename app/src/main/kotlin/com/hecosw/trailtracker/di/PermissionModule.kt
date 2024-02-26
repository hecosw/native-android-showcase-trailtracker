package com.hecosw.trailtracker.di

import com.hecosw.trailtracker.ui.location.LocationPermissionChecker
import com.hecosw.trailtracker.ui.location.LocationPermissionCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // Use SingletonComponent for application-wide singletons
abstract class PermissionrModule {

    @Binds
    abstract fun bindLocationPermissionChecker(
        impl: LocationPermissionCheckerImpl
    ): LocationPermissionChecker
}
