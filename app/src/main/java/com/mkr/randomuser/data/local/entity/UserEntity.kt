package com.mkr.randomuser.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gender: String,
    @Embedded val name: NameEntity,
    @Embedded val location: LocationEntity,
    @Embedded val login: LoginEntity,
    @Embedded val dob: DobEntity,
    val email: String,
    val phone: String,
    val cell: String,
    @Embedded val picture: PictureEntity,
    val nat: String
)