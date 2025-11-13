package com.mkr.randomuser.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Name(
    val title: String,
    val first: String,
    val last: String
) : Parcelable