package com.hecosw.trailtracker.data.source.framework

import com.hecosw.trailtracker.BuildConfig
import com.hecosw.trailtracker.data.model.dto.PhotoDto
import com.hecosw.trailtracker.data.source.network.PhotoApi
import com.hecosw.trailtracker.domain.value.Geolocation
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ktor.getApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class FlickrPhotoApi @Inject constructor(private val client: HttpClient) : PhotoApi {

    override suspend fun getLocationPhoto(location: Geolocation): ApiResponse<PhotoDto> {
        try {
            return client.getApiResponse<PhotoDto> {
                contentType(ContentType.Application.Json)
                parameter(KEY_METHOD, VALUE_METHOD)
                parameter(KEY_API_KEY, VALUE_API_KEY)
                parameter(KEY_FORMAT, VALUE_FORMAT)
                parameter(KEY_NO_JSON_CALLBACK, VALUE_NO_JSON_CALLBACK)
                parameter(KEY_RADIUS_UNITS, VALUE_RADIUS_UNITS)
                parameter(KEY_RADIUS, VALUE_RADIUS)
                parameter(KEY_PER_PAGE, VALUE_PER_PAGE)
                parameter(KEY_ACCURACY, VALUE_ACCURACY)
                parameter(KEY_EXTRAS, VALUE_EXTRAS)
                parameter(KEY_SORT, VALUE_SORT)
                parameter(KEY_CONTENT_TYPES, VALUE_CONTENT_TYPES)
                parameter(KEY_MEDIA, VALUE_MEDIA)
                parameter(KEY_LATITUDE, location.latitude.value.toString())
                parameter(KEY_LONGITUDE, location.longitude.value.toString())
            }
        } catch (e: Exception) {
            return ApiResponse.Failure.Exception(e)
        }
    }

    companion object {
        private const val VALUE_METHOD = "flickr.photos.search"
        private const val VALUE_API_KEY = BuildConfig.TRAILTRACKER_FLICKR_API_KEY
        private const val VALUE_FORMAT = "json"
        private const val VALUE_NO_JSON_CALLBACK = "1"
        private const val VALUE_RADIUS_UNITS = "km"
        private const val VALUE_RADIUS = "0.15"
        private const val VALUE_PER_PAGE = "1"
        private const val VALUE_ACCURACY = "16"
        private const val VALUE_EXTRAS = "url_s"
        private const val VALUE_SORT = "date-taken-desc"
        private const val VALUE_CONTENT_TYPES = "0"
        private const val VALUE_MEDIA = "photos"

        private const val KEY_METHOD = "method"
        private const val KEY_API_KEY = "api_key"
        private const val KEY_FORMAT = "format"
        private const val KEY_NO_JSON_CALLBACK = "nojsoncallback"
        private const val KEY_RADIUS_UNITS = "radius_units"
        private const val KEY_RADIUS = "radius"
        private const val KEY_PER_PAGE = "per_page"
        private const val KEY_ACCURACY = "accuracy"
        private const val KEY_EXTRAS = "extras"
        private const val KEY_SORT = "sort"
        private const val KEY_CONTENT_TYPES = "content_types"
        private const val KEY_MEDIA = "media"
        private const val KEY_LATITUDE = "lat"
        private const val KEY_LONGITUDE = "lon"
    }
}
