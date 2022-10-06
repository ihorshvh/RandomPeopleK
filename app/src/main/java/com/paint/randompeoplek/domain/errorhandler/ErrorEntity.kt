package com.paint.randompeoplek.domain.errorhandler

sealed class ErrorEntity {

    object Network : ErrorEntity()

    object ServiceUnavailable : ErrorEntity()

    object Unknown : ErrorEntity()

    object NoError : ErrorEntity()
}