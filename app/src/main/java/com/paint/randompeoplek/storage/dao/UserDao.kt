package com.paint.randompeoplek.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.paint.randompeoplek.storage.entity.User
import java.util.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getAll() : List<User>

    @Query("SELECT * FROM user WHERE id == :id")
    suspend fun getUserById(id: String) : User

    @Insert
    suspend fun insertAll(users : List<User>)

    @Query("SELECT COUNT(*) FROM user WHERE lastUpdated >= :timeout")
    suspend fun hasUser(timeout : Date) : Int

    @Query("DELETE FROM user")
    suspend fun deleteAll()

}