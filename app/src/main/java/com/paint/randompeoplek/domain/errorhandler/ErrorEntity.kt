package com.paint.randompeoplek.domain.errorhandler

sealed class ErrorEntity {
    data object Network : ErrorEntity()
    data object ServiceUnavailable : ErrorEntity()
    data object Unknown : ErrorEntity()
}