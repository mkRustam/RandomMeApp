package com.mkr.randomuser.data.remote.dto

data class UserDto(
    val gender: String,
    val name: NameDto,
    val location: LocationDto,
    val email: String,
    val login: LoginDto,
    val dob: DobDto,
    val phone: String,
    val cell: String,
    val picture: PictureDto,
    val nat: String
)