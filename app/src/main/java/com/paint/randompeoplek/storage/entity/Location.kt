package com.paint.randompeoplek.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class Location(
    @Embedded val street : Street,
    @ColumnInfo(name = "city") val city : String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "country") val country : String,
    @ColumnInfo(name = "postcode") val postCode: String
)