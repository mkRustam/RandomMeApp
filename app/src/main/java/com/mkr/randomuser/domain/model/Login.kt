package com.mkr.randomuser.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Login(
    val uuid: String,
    val username: String,
    val password: String
) : Parcelable