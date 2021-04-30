package com.paint.randompeoplek.model

import com.google.gson.annotations.SerializedName

data class UserResponse(@SerializedName("results") val users: List<User>)