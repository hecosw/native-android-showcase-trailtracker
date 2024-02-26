package com.hecosw.trailtracker.domain.value

@JvmInline
value class Latitude(val value: Double)

@JvmInline
value class Longitude(val value: Double)

data class Geolocation(val latitude: Latitude, val longitude: Longitude)
