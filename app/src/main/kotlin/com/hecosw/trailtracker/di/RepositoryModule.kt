package com.hecosw.trailtracker.di

import com.hecosw.trailtracker.common.logging.Logger
import com.hecosw.trailtracker.data.source.location.LocationProvider
import com.hecosw.trailtracker.data.model.mapper.PhotoDtoMapper
import com.hecosw.trailtracker.data.source.network.PhotoApi
import com.hecosw.trailtracker.data.repository.PhotoRepositoryImpl
import com.hecosw.trailtracker.data.repository.LocationRepositoryImpl
import com.hecosw.trailtracker.domain.port.LocationRepository
import com.hecosw.trailtracker.domain.port.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLocationRepository(locationProvider: LocationProvider): LocationRepository {
        return LocationRepositoryImpl(locationProvider)
    }

    @Provides
    @Singleton
    fun providePhotoRepository(photoApi: PhotoApi, photoDtoMapper: PhotoDtoMapper, log: Logger): PhotoRepository {
        return PhotoRepositoryImpl(photoApi, photoDtoMapper, log)
    }

}
