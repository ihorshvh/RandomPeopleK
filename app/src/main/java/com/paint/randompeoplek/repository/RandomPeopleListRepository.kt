package com.paint.randompeoplek.repository

import com.paint.randompeoplek.service.RandomPeopleService
import com.paint.randompeoplek.repository.model.UserResponse
import com.paint.randompeoplek.storage.dao.UserDao
import com.paint.randompeoplek.storage.entity.*
import java.util.Date
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RandomPeopleListRepository @Inject constructor(
    private val randomPeopleService: RandomPeopleService,
    private val userDao: UserDao
) {

    suspend fun getUserList(userQuantity: String) : UserResponse {

        val freshUsersNumber = userDao.hasUser(getTimeout())

        if (freshUsersNumber <= 0) {
            try {
                val userResponse = randomPeopleService.getUserList(userQuantity)

                userDao.deleteAll()
                userDao.insertAll(userResponse.users.toStorageUsers())
            } catch (e : Exception) {
                return UserResponse(userDao.getAll().toRepositoryUsers(), e)
            }
        }

        return UserResponse(userDao.getAll().toRepositoryUsers())
    }

    suspend fun getUserById(userId: String): com.paint.randompeoplek.repository.model.User {
        return userDao.getUserById(userId).toRepositoryUser()
    }

    companion object {

        private val FRESH_TIMEOUT = TimeUnit.SECONDS.toMillis(15)

        fun getTimeout() = Date(Calendar.getInstance().time.time - FRESH_TIMEOUT)
    }
}

private fun Location.toRepositoryLocation() =
    com.paint.randompeoplek.repository.model.Location(this.street.toRepositoryStreet(),
        this.city,
        this.state,
        this.country,
        this.postCode)

private fun Name.toRepositoryName() =
    com.paint.randompeoplek.repository.model.Name(this.title, this.firstName, this.lastName)

private fun Picture.toRepositoryPicture() =
    com.paint.randompeoplek.repository.model.Picture(this.large, this.medium, this.thumbnail)

private fun Street.toRepositoryStreet() =
    com.paint.randompeoplek.repository.model.Street(this.number, this.name)

private fun User.toRepositoryUser() =
    com.paint.randompeoplek.repository.model.User(
        this.id,
        this.name.toRepositoryName(),
        this.location.toRepositoryLocation(),
        this.email,
        this.phone,
        this.picture.toRepositoryPicture())

private fun List<User>.toRepositoryUsers() = this.map { it.toRepositoryUser() }

private fun com.paint.randompeoplek.service.model.Location.toStorageLocation() =
    com.paint.randompeoplek.storage.entity.Location(this.street.toStorageStreet(),
        this.city,
        this.state,
        this.country,
        this.postCode)

private fun com.paint.randompeoplek.service.model.Name.toStorageName() =
    com.paint.randompeoplek.storage.entity.Name(this.title, this.firstName, this.lastName)

private fun com.paint.randompeoplek.service.model.Picture.toStoragePicture() =
    com.paint.randompeoplek.storage.entity.Picture(this.large, this.medium, this.thumbnail)

private fun com.paint.randompeoplek.service.model.Street.toStorageStreet() =
    com.paint.randompeoplek.storage.entity.Street(this.number, this.name)

private fun com.paint.randompeoplek.service.model.User.toStorageUser() =
    com.paint.randompeoplek.storage.entity.User(
        this.id.uuid,
        this.name.toStorageName(),
        this.location.toStorageLocation(),
        this.email,
        this.phone,
        this.picture.toStoragePicture(),
        Calendar.getInstance().time
    )

private fun List<com.paint.randompeoplek.service.model.User>.toStorageUsers() = this.map { it.toStorageUser() }