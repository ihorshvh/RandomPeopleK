package com.paint.randompeoplek.model

import com.google.gson.annotations.SerializedName

data class Street (
    @SerializedName("number") val number: Long,
    @SerializedName("name") val name: String
)
