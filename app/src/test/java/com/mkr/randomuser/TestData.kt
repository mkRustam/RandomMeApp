package com.mkr.randomuser

import com.mkr.randomuser.domain.model.*

object TestData {
    fun createTestUser(
        id: Int = 1,
        gender: String = "male",
        firstName: String = "John",
        lastName: String = "Doe",
        email: String = "john.doe@example.com",
        phone: String = "123-456-7890",
        nat: String = "US"
    ): User {
        return User(
            id = id,
            gender = gender,
            name = Name(title = "Mr", first = firstName, last = lastName),
            location = Location(
                street = Street(number = 123, name = "Main St"),
                city = "New York",
                state = "NY",
                country = "United States",
                postcode = "10001"
            ),
            login = Login(
                uuid = "test-uuid-$id",
                username = "johndoe$id",
                password = "password123"
            ),
            dob = Dob(date = "1990-01-01T00:00:00.000Z", age = 34),
            email = email,
            phone = phone,
            cell = "098-765-4321",
            picture = Picture(
                large = "https://example.com/large.jpg",
                medium = "https://example.com/medium.jpg",
                thumbnail = "https://example.com/thumbnail.jpg"
            ),
            nat = nat
        )
    }

    fun createTestUsers(count: Int): List<User> {
        return (1..count).map { createTestUser(id = it, firstName = "User$it") }
    }
}

