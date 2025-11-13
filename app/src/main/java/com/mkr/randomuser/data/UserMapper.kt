package com.mkr.randomuser.data

import com.mkr.randomuser.data.local.entity.CoordinatesEntity
import com.mkr.randomuser.data.local.entity.DobEntity
import com.mkr.randomuser.data.local.entity.LocationEntity
import com.mkr.randomuser.data.local.entity.LoginEntity
import com.mkr.randomuser.data.local.entity.NameEntity
import com.mkr.randomuser.data.local.entity.PictureEntity
import com.mkr.randomuser.data.local.entity.StreetEntity
import com.mkr.randomuser.data.local.entity.TimezoneEntity
import com.mkr.randomuser.data.local.entity.UserEntity
import com.mkr.randomuser.data.remote.dto.UserDto
import com.mkr.randomuser.domain.model.Dob
import com.mkr.randomuser.domain.model.Location
import com.mkr.randomuser.domain.model.Login
import com.mkr.randomuser.domain.model.Name
import com.mkr.randomuser.domain.model.Picture
import com.mkr.randomuser.domain.model.Street
import com.mkr.randomuser.domain.model.User

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        gender = gender,
        name = NameEntity(name.title, name.first, name.last),
        location = LocationEntity(
            street = StreetEntity(location.street.number, location.street.name),
            city = location.city,
            state = location.state,
            country = location.country,
            postcode = location.postcode,
            coordinates = location.coordinates?.let { CoordinatesEntity(it.latitude, it.longitude) },
            timezone = location.timezone?.let { TimezoneEntity(it.offset, it.description) }
        ),
        login = LoginEntity(login.uuid, login.username, login.password),
        dob = DobEntity(dob.date, dob.age),
        email = email,
        phone = phone,
        cell = cell,
        picture = PictureEntity(picture.large, picture.medium, picture.thumbnail),
        nat = nat
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        gender = gender,
        name = Name(name.title, name.first, name.last),
        location = Location(
            street = Street(location.street.number, location.street.name),
            city = location.city,
            state = location.state,
            country = location.country,
            postcode = location.postcode
        ),
        login = Login(login.uuid, login.username, login.password),
        dob = Dob(dob.date, dob.age),
        email = email,
        phone = phone,
        cell = cell,
        picture = Picture(picture.large, picture.medium, picture.thumbnail),
        nat = nat
    )
}