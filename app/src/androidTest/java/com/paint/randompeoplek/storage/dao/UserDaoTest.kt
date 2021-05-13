package com.paint.randompeoplek.storage.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.paint.randompeoplek.storage.AppDatabase
import com.paint.randompeoplek.storage.entity.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var tesDatabase : AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        tesDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = tesDatabase.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = runBlocking {
        userDao.deleteAll()
        tesDatabase.close()
    }

    @Test
    fun testInsertAll() = runBlocking {
        var users = getTestEntityUsers()

        userDao.insertAll(users)
        users = userDao.getAll()

        assertFalse(users.isEmpty())
        assertEquals(2, users.size)

        userDao.deleteAll()
        users = userDao.getAll()
        assertTrue(users.isEmpty())
    }

    @Test
    fun testHasUser() = runBlocking {
        val user = getTestEntityUser()
        userDao.insertAll(listOf(user))

        val date = Date(Calendar.getInstance().time.time)
        var usersNumber = userDao.hasUser(date)
        assertEquals(0, usersNumber)

        val earlyDate = Date(date.time - TimeUnit.SECONDS.toMillis(16))
        usersNumber = userDao.hasUser(earlyDate)
        assertEquals(1, usersNumber)

        userDao.deleteAll()
    }

    private fun getTestEntityUsers() : List<User> {
        val name = Name("Mr", "Ryan", "Wilson")
        val street = Street(740, "Lambie Drive")
        val location = Location(street, "Invercargill", "Bay of Plenty", "New Zealand", "32336")
        val user = User(name, location, "", "", Picture("", "", ""), Date())

        val secondName = Name("Mr", "Ryan", "Wilson")
        val secondStreet = Street(740, "Lambie Drive")
        val secondLocation = Location(secondStreet, "Invercargill", "Bay of Plenty", "New Zealand", "32336")
        val secondUser = User(secondName, secondLocation, "", "", Picture("", "", ""), Date())

        return listOf(user, secondUser)
    }


    private fun getTestEntityUser() : User {
        val name = Name("Mr", "Ryan", "Wilson")
        val street = Street(740, "Lambie Drive")
        val location = Location(street, "Invercargill", "Bay of Plenty", "New Zealand", "32336")
        return User(name, location, "", "", Picture("", "", ""), Date())
    }
}