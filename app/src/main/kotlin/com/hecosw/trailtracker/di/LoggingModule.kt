package com.hecosw.trailtracker.di

import com.hecosw.trailtracker.common.logging.Logger
import com.hecosw.trailtracker.common.logging.TrailTrackerLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggingModule {

    @Binds
    @Singleton
    abstract fun bindLogger(impl: TrailTrackerLogger): Logger
}
