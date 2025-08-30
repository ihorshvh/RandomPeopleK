package com.paint.randompeoplek.repository

enum class NetworkError {
    REQUEST_TIMEOUT,
    UNAUTHORIZED,
    CONFLICT,
    NO_INTERNET,
    PAYLOAD_TOO_LARGE,
    SERVER_ERROR,
    UNKNOWN;
}