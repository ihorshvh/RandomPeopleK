package com.paint.randompeoplek.model

sealed class Response<T>(
    val data: T? = null,
    val errorMessage: String? = null
) {
    class Initial<T> : Response<T>()
    class Success<T>(data: T) : Response<T>(data)
    class Error<T>(errorMessage: String, data: T? = null) : Response<T>(data, errorMessage)
}
