package com.paint.randompeoplek.storage.entity

import androidx.room.ColumnInfo

data class Street (
    @ColumnInfo(name = "number") val number: Long,
    @ColumnInfo(name = "name") val name: String
)
