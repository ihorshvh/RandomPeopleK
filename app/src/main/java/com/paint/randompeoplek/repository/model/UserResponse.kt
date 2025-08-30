package com.paint.randompeoplek.repository.model

import com.paint.randompeoplek.repository.NetworkError

class UserResponse {
    val users: List<User>
    val networkError: NetworkError?

    constructor(users: List<User>) {
        this.users = users
        this.networkError = null
    }

    constructor(users: List<User>, networkError: NetworkError) {
        this.users = users
        this.networkError = networkError
    }
}