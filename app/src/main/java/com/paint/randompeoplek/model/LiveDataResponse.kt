package com.paint.randompeoplek.model

class LiveDataResponse<Response> {

    var response: Response? = null

    var error: String? = null

    constructor(response: Response) {
        this.response = response
    }

    constructor(error: String?) {
        this.error = error
    }

    constructor(response: Response, error: String) {
        this.response = response
        this.error = error
    }

}