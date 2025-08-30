package com.paint.randompeoplek.domain.errorhandler

import com.paint.randompeoplek.repository.NetworkError
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface ErrorHandlerUseCase {

    fun getErrorEntity(throwable: Throwable?): ErrorEntity
    fun getErrorEntity(networkError: NetworkError?): ErrorEntity

}

class ErrorHandlerUseCaseImpl @Inject constructor() : ErrorHandlerUseCase {

    override fun getErrorEntity(throwable: Throwable?): ErrorEntity {
        return when(throwable) {
            is IOException -> ErrorEntity.Network
            is HttpException -> ErrorEntity.ServiceUnavailable
            else -> ErrorEntity.Unknown
        }
    }

    override fun getErrorEntity(networkError: NetworkError?): ErrorEntity {
        return ErrorEntity.Unknown
    }
}