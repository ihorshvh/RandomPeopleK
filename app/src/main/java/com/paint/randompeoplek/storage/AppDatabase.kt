package com.paint.randompeoplek.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paint.randompeoplek.storage.dao.UserDao
import com.paint.randompeoplek.storage.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}