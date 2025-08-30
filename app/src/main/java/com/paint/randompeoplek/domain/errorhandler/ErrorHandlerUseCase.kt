package com.paint.randompeoplek.domain.errorhandler

import com.paint.randompeoplek.R
import com.paint.randompeoplek.repository.NetworkError
import com.paint.randompeoplek.resourceprovider.ResourceProvider
import javax.inject.Inject

interface ErrorHandlerUseCase {

    fun getErrorMessage(throwable: Throwable?): String

    fun getErrorMessage(networkError: NetworkError?): String
}

class ErrorHandlerUseCaseImpl @Inject constructor(
    private val resourceProvider: ResourceProvider
) : ErrorHandlerUseCase {

    override fun getErrorMessage(throwable: Throwable?): String {
        return getErrorMessage(NetworkError.UNKNOWN)
    }

    override fun getErrorMessage(networkError: NetworkError?): String {
        return when (networkError?.name) {
            NetworkError.REQUEST_TIMEOUT.name -> {
                resourceProvider.getString(R.string.error_timeout)
            }
            NetworkError.UNAUTHORIZED.name -> {
                resourceProvider.getString(R.string.error_unauthorized)
            }
            NetworkError.NO_INTERNET.name -> {
                resourceProvider.getString(R.string.error_no_internet)
            }

            NetworkError.SERVER_ERROR.name -> {
                resourceProvider.getString(R.string.error_server)
            }

            NetworkError.UNKNOWN.name -> {
                resourceProvider.getString(R.string.error_unknown)
            }

            else -> {
                resourceProvider.getString(R.string.error_unknown)
            }
        }
    }
}