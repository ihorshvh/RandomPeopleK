package com.paint.randompeoplek.model

import com.paint.randompeoplek.domain.errorhandler.ErrorEntity

sealed class LoadResult<T>(
    val data: T? = null,
    val errorEntity: ErrorEntity? = null
) {
    class Initial<T> : LoadResult<T>()
    class Success<T>(data: T) : LoadResult<T>(data)
    class Loading<T>(data: T? = null) : LoadResult<T>(data)
    class Error<T>(errorEntity: ErrorEntity, data: T? = null) : LoadResult<T>(data, errorEntity)
}
