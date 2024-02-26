package com.hecosw.trailtracker.data.model.mapper

import com.hecosw.trailtracker.data.model.dto.PhotoDto
import com.hecosw.trailtracker.domain.entity.Photo
import com.hecosw.trailtracker.domain.value.PhotoId
import com.hecosw.trailtracker.domain.value.PhotoTitle
import com.hecosw.trailtracker.domain.value.PhotoUrl
import javax.inject.Inject

class PhotoDtoMapper @Inject constructor() : DataToDomainModelMapper<PhotoDto, Photo> {
    override fun map(dataModel: PhotoDto): Photo {
        val photoItem = dataModel.photos?.photo?.first()
        return Photo(
            PhotoId(photoItem?.id?.toLong () ?: -1L),
            PhotoTitle(photoItem?.title ?: ""),
            PhotoUrl(photoItem?.urlS ?: "")
        )
    }
}
