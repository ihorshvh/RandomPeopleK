package com.paint.randompeoplek.model

import com.paint.randompeoplek.domain.errorhandler.ErrorEntity

sealed class Response<T>(
    val data: T? = null,
    val errorEntity: ErrorEntity? = null
) {
    class Initial<T> : Response<T>()
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(errorEntity: ErrorEntity, data: T? = null) : Response<T>(data, errorEntity)
}
