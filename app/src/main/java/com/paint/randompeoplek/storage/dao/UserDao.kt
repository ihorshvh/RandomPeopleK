package com.paint.randompeoplek.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.paint.randompeoplek.storage.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll() : List<User>

    @Insert
    fun insertAll(users : List<User>)

    @Query("DELETE FROM user")
    fun deleteAll()

}