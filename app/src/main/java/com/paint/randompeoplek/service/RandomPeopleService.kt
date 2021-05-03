package com.paint.randompeoplek.service

import com.paint.randompeoplek.service.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomPeopleService {

    @GET("?inc=name,email,phone,location,picture")
    suspend fun getUserList(@Query("results") userQuantity: String): UserResponse

}