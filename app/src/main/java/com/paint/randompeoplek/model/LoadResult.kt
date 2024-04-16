package com.paint.randompeoplek.model

import com.paint.randompeoplek.domain.errorhandler.ErrorEntity

sealed class LoadResult<T>(
    val data: T? = null,
    val errorEntity: ErrorEntity? = null
) {
    class Initial<T>(data: T? = null) : LoadResult<T>(data) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    class Success<T>(data: T) : LoadResult<T>(data) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    class Loading<T>(data: T? = null) : LoadResult<T>(data) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    class Error<T>(errorEntity: ErrorEntity, data: T? = null) : LoadResult<T>(data, errorEntity) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }
}
