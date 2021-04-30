package com.paint.randompeoplek.model

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("name") val name : Name,
    @SerializedName("location") val location : Location,
    @SerializedName("email") val email : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("picture") val picture : Picture)
