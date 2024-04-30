package com.paint.randompeoplek.storage.entity

import androidx.room.ColumnInfo

data class Name(
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "first") val firstName : String,
    @ColumnInfo(name = "last") val lastName : String
)
