package com.hecosw.trailtracker.di

import GmsLocationProvider
import android.content.Context
import com.hecosw.trailtracker.common.logging.Logger
import com.hecosw.trailtracker.data.source.location.LocationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideLocationService(
        @ApplicationContext context: Context,
        log: Logger
    ): LocationProvider {
        return GmsLocationProvider(context, log)
    }
}
