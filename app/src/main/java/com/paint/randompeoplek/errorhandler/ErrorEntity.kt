package com.paint.randompeoplek.errorhandler

sealed class ErrorEntity {

    object Network : ErrorEntity()

    object ServiceUnavailable : ErrorEntity()

    object Unknown : ErrorEntity()

}