package com.hecosw.trailtracker.di

import com.hecosw.trailtracker.domain.interactor.GetLocationPhotoInteractor
import com.hecosw.trailtracker.domain.interactor.StartLocationUpdatesInteractor
import com.hecosw.trailtracker.domain.interactor.StopLocationUpdatesInteractor
import com.hecosw.trailtracker.domain.interactor.SubscribeLocationUpdatesInteractor
import com.hecosw.trailtracker.domain.port.LocationRepository
import com.hecosw.trailtracker.domain.port.PhotoRepository
import com.hecosw.trailtracker.domain.usecase.GetLocationPhotoUseCase
import com.hecosw.trailtracker.domain.usecase.StartLocationUpdatesUseCase
import com.hecosw.trailtracker.domain.usecase.StopLocationUpdatesUseCase
import com.hecosw.trailtracker.domain.usecase.SubscribeLocationUpdatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetLocationPhotoUseCase(photoRepository: PhotoRepository): GetLocationPhotoUseCase {
        return GetLocationPhotoInteractor(photoRepository)
    }

    @Provides
    @Singleton
    fun provideStartLocationServiceUseCase(
        locationRepository: LocationRepository
    ): StartLocationUpdatesUseCase {
        return StartLocationUpdatesInteractor(locationRepository)
    }

    @Provides
    @Singleton
    fun provideStopLocationServiceUseCase(
        locationRepository: LocationRepository
    ): StopLocationUpdatesUseCase {
        return StopLocationUpdatesInteractor(locationRepository)
    }

    @Provides
    @Singleton
    fun provideSubscribeLocationUpdatesInteractor(
        locationRepository: LocationRepository
    ): SubscribeLocationUpdatesUseCase {
        return SubscribeLocationUpdatesInteractor(locationRepository)
    }

}
