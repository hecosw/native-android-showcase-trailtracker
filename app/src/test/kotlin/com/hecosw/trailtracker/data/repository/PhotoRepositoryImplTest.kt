package com.hecosw.trailtracker.data.repository

import arrow.core.getOrElse
import com.hecosw.trailtracker.common.logging.Logger
import com.hecosw.trailtracker.data.model.dto.PhotoItem
import com.hecosw.trailtracker.data.source.network.PhotoApi
import com.hecosw.trailtracker.domain.value.Geolocation
import com.hecosw.trailtracker.data.model.dto.PhotoDto
import com.hecosw.trailtracker.data.model.dto.PhotoList
import com.hecosw.trailtracker.data.model.mapper.PhotoDtoMapper
import com.hecosw.trailtracker.domain.entity.Photo
import com.hecosw.trailtracker.domain.value.Latitude
import com.hecosw.trailtracker.domain.value.Longitude
import com.skydoves.sandwich.ApiResponse

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PhotoRepositoryImplTest {

    private lateinit var photoApi: PhotoApi
    private lateinit var photoDtoMapper: PhotoDtoMapper
    private lateinit var log: Logger
    private lateinit var photoRepository: PhotoRepositoryImpl

    @BeforeEach
    fun setUp() {
        photoApi = mockk(relaxed = true)
        photoDtoMapper = mockk(relaxed = true)
        log = mockk(relaxed = true)

        photoRepository = PhotoRepositoryImpl(photoApi, photoDtoMapper, log)
    }

    @Test
    fun `getLocationPhoto returns photo on successful response`() = runBlocking {
        // Given
        val geolocation = Geolocation(Latitude(1.0), Longitude(2.0))
        val photoDto = PhotoDto(photos = PhotoList(photo = listOf(PhotoItem())))
        val photo = Photo()
        coEvery { photoApi.getLocationPhoto(geolocation) } returns ApiResponse.Success(photoDto)
        every { photoDtoMapper.map(any()) } returns photo

        // When
        val result = photoRepository.getLocationPhoto(geolocation)

        // Then
        assertTrue(result.isRight())
        assertEquals(photo, result.getOrElse { null })
        coVerify(exactly = 1) { photoApi.getLocationPhoto(geolocation) }
        verify { photoDtoMapper.map(any()) }
    }

}
