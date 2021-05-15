package com.paint.randompeoplek.model

sealed class LoadResult<T>(
    val data: T? = null,
    val message: String? = null
) {
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

    class Error<T>(message: String, data: T? = null) : LoadResult<T>(data, message) {
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
