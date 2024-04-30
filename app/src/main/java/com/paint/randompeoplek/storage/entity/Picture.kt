package com.paint.randompeoplek.storage.entity

import androidx.room.ColumnInfo

data class Picture(
    @ColumnInfo(name = "large") val large : String,
    @ColumnInfo(name = "medium") val medium : String,
    @ColumnInfo(name = "thumbnail") val thumbnail : String
)
