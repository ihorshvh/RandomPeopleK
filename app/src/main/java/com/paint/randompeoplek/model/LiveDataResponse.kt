package com.paint.randompeoplek.model

class LiveDataResponse<Response> {

    var response: Response? = null

    var warning: String? = null

    constructor(response: Response) {
        this.response = response
    }

    constructor(error: String?) {
        this.warning = error
    }

    constructor(response: Response, error: String) {
        this.response = response
        this.warning = error
    }

}