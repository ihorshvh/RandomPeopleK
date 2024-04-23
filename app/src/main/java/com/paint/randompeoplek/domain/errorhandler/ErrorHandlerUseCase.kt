package com.paint.randompeoplek.domain.errorhandler

import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface ErrorHandlerUseCase {

    fun getErrorEntity(throwable: Throwable?): ErrorEntity

}

class ErrorHandlerUseCaseImpl @Inject constructor() : ErrorHandlerUseCase {

    override fun getErrorEntity(throwable: Throwable?): ErrorEntity {
        return when(throwable) {
            is IOException -> ErrorEntity.Network
            is HttpException -> ErrorEntity.ServiceUnavailable
            else -> ErrorEntity.Unknown
        }
    }
}