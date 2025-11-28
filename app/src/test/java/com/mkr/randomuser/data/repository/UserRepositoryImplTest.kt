package com.mkr.randomuser.data.repository

import com.mkr.randomuser.TestData
import com.mkr.randomuser.core.common.Resource
import com.mkr.randomuser.core.coroutines.DispatcherProvider
import com.mkr.randomuser.data.local.UserDao
import com.mkr.randomuser.data.remote.ApiService
import com.mkr.randomuser.data.remote.dto.ApiResponseDto
import com.mkr.randomuser.data.remote.dto.UserDto
import com.mkr.randomuser.data.toDomain
import com.mkr.randomuser.data.toEntity
import com.mkr.randomuser.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var userDao: UserDao
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var repository: UserRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        apiService = mockk()
        userDao = mockk()
        dispatcherProvider = mockk()
        
        every { dispatcherProvider.io } returns testDispatcher
        
        repository = UserRepositoryImpl(apiService, userDao, dispatcherProvider)
    }

    @Test
    fun `createRandomUser returns Success when API and database succeed`() = runTest(testDispatcher) {
        val gender = "male"
        val nat = "US"
        val testUser = TestData.createTestUser()
        val userEntity = testUser.toEntity()
        val userDto = createTestUserDto()

        coEvery { apiService.createRandomUser(gender, nat) } returns ApiResponseDto(listOf(userDto))
        coEvery { userDao.insertUser(any()) } returns 1L
        coEvery { userDao.getUserById(1) } returns userEntity

        val result = repository.createRandomUser(gender, nat)

        assertTrue(result is Resource.Success)
        val successResult = result as Resource.Success
        assertEquals(testUser.id, successResult.data.id)
        assertEquals(testUser.email, successResult.data.email)
        
        coVerify { apiService.createRandomUser(gender, nat) }
        coVerify { userDao.insertUser(any()) }
        coVerify { userDao.getUserById(1) }
    }

    @Test
    fun `createRandomUser returns Error when API fails`() = runTest(testDispatcher) {
        val gender = "male"
        val nat = "US"
        val exception = RuntimeException("Network error")

        coEvery { apiService.createRandomUser(gender, nat) } throws exception

        val result = repository.createRandomUser(gender, nat)

        assertTrue(result is Resource.Error)
        assertEquals("Network error", (result as Resource.Error).message)
    }

    @Test
    fun `createRandomUser returns Error when API returns empty results`() = runTest(testDispatcher) {
        val gender = "male"
        val nat = "US"

        coEvery { apiService.createRandomUser(gender, nat) } returns ApiResponseDto(emptyList())

        val result = repository.createRandomUser(gender, nat)

        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message.contains("No user returned"))
    }

    @Test
    fun `getSavedUsers returns flow of users from database`() = runTest {
        val testUsers = TestData.createTestUsers(3)
        val userEntities = testUsers.map { it.toEntity() }

        every { userDao.getAllUsers() } returns flowOf(userEntities)

        val result = repository.getSavedUsers()
        val collectedUsers = mutableListOf<User>()
        result.collect { collectedUsers.addAll(it) }

        assertEquals(3, collectedUsers.size)
        assertEquals(testUsers[0].id, collectedUsers[0].id)
    }

    @Test
    fun `getSavedUsers returns empty flow when database is empty`() = runTest {
        every { userDao.getAllUsers() } returns flowOf(emptyList())

        val result = repository.getSavedUsers()
        val collectedUsers = mutableListOf<List<User>>()
        result.collect { collectedUsers.add(it) }

        assertEquals(1, collectedUsers.size)
        assertTrue(collectedUsers.first().isEmpty())
    }

    @Test
    fun `getUserById returns Success when user exists`() = runTest(testDispatcher) {
        val userId = 1
        val testUser = TestData.createTestUser(id = userId)
        val userEntity = testUser.toEntity()

        coEvery { userDao.getUserById(userId) } returns userEntity

        val result = repository.getUserById(userId)

        assertTrue(result is Resource.Success)
        assertEquals(testUser, (result as Resource.Success).data)
        coVerify { userDao.getUserById(userId) }
    }

    @Test
    fun `getUserById returns Error when user not found`() = runTest(testDispatcher) {
        val userId = 999
        val exception = RuntimeException("User not found")

        coEvery { userDao.getUserById(userId) } throws exception

        val result = repository.getUserById(userId)

        assertTrue(result is Resource.Error)
        assertEquals("User not found", (result as Resource.Error).message)
    }

    private fun createTestUserDto(): UserDto {
        val testUser = TestData.createTestUser()
        return UserDto(
            gender = testUser.gender,
            name = com.mkr.randomuser.data.remote.dto.NameDto(
                title = testUser.name.title,
                first = testUser.name.first,
                last = testUser.name.last
            ),
            location = com.mkr.randomuser.data.remote.dto.LocationDto(
                street = com.mkr.randomuser.data.remote.dto.StreetDto(
                    number = testUser.location.street.number,
                    name = testUser.location.street.name
                ),
                city = testUser.location.city,
                state = testUser.location.state,
                country = testUser.location.country,
                postcode = testUser.location.postcode,
                coordinates = com.mkr.randomuser.data.remote.dto.CoordinatesDto("0.0", "0.0"),
                timezone = com.mkr.randomuser.data.remote.dto.TimezoneDto("+00:00", "UTC")
            ),
            email = testUser.email,
            login = com.mkr.randomuser.data.remote.dto.LoginDto(
                uuid = testUser.login.uuid,
                username = testUser.login.username,
                password = testUser.login.password,
                salt = "",
                md5 = "",
                sha1 = "",
                sha256 = ""
            ),
            dob = com.mkr.randomuser.data.remote.dto.DobDto(
                date = testUser.dob.date,
                age = testUser.dob.age
            ),
            phone = testUser.phone,
            cell = testUser.cell,
            picture = com.mkr.randomuser.data.remote.dto.PictureDto(
                large = testUser.picture.large,
                medium = testUser.picture.medium,
                thumbnail = testUser.picture.thumbnail
            ),
            nat = testUser.nat
        )
    }
}

