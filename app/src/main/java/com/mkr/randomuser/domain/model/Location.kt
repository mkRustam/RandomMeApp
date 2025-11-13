package com.mkr.randomuser.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String
) : Parcelable