package com.mkr.randomuser.data.local.entity

import androidx.room.Embedded

data class LocationEntity(
    @Embedded val street: StreetEntity,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    @Embedded val coordinates: CoordinatesEntity?,
    @Embedded val timezone: TimezoneEntity?
)