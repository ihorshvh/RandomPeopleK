package com.paint.randompeoplek.errorhandler

import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface ErrorHandler {

    fun getErrorEntity(throwable: Throwable): ErrorEntity

}

class ErrorHandlerImpl @Inject constructor() : ErrorHandler {

    override fun getErrorEntity(throwable: Throwable): ErrorEntity {
        return when(throwable) {
            is IOException -> ErrorEntity.Network
            is HttpException -> ErrorEntity.ServiceUnavailable
            else -> ErrorEntity.Unknown
        }
    }
}