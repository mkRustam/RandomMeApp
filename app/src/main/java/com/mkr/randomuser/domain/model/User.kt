package com.mkr.randomuser.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val gender: String,
    val name: Name,
    val location: Location,
    val login: Login,
    val dob: Dob,
    val email: String,
    val phone: String,
    val cell: String,
    val picture: Picture,
    val nat: String
) : Parcelable

