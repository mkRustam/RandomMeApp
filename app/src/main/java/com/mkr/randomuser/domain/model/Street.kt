package com.mkr.randomuser.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Street(
    val number: Int,
    val name: String
) : Parcelable