package com.paint.randompeoplek.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,

    @Embedded val name : Name,
    @Embedded val location : Location,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "phone") val phone : String,
    @Embedded val picture : Picture,
    @ColumnInfo(name = "lastUpdated") val date : Date) {
}
