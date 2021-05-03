package com.paint.randompeoplek.model

class LiveDataResponse<Response> {

    var response: Response? = null
        private set

    var error: String? = null
        private set

    constructor(response: Response) {
        this.response = response
    }

    constructor(error: String?) {
        this.error = error
    }

}