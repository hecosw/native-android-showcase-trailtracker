package com.hecosw.trailtracker.di

import com.hecosw.trailtracker.data.model.dto.PhotoDto
import com.hecosw.trailtracker.data.model.mapper.DataToDomainModelMapper
import com.hecosw.trailtracker.data.model.mapper.PhotoDtoMapper
import com.hecosw.trailtracker.domain.entity.Photo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class) // Or whichever component scope is appropriate
object MapperModule {

    @Provides
    fun providePhotoDtoMapper(): DataToDomainModelMapper<PhotoDto, Photo> = PhotoDtoMapper()

}
