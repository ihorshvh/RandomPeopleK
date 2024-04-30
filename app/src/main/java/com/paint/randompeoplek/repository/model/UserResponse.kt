package com.paint.randompeoplek.repository.model

class UserResponse {

    val users: List<User>

    var throwable: Throwable? = null

    constructor(users: List<User>) {
        this.users = users
    }

    constructor(users: List<User>, throwable: Throwable) {
        this.users = users
        this.throwable = throwable
    }
}