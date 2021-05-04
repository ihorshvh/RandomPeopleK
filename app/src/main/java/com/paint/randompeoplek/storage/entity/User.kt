package com.paint.randompeoplek.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val uid : Int,
    @Embedded val name : Name,
    @Embedded val location : Location,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "phone") val phone : String,
    @Embedded val picture : Picture)
