package com.paint.randompeoplek.model

class LiveDataResponse<Response> {

    var response: Response? = null

    var warningThrowable: Throwable? = null

    constructor(response: Response) {
        this.response = response
    }

    constructor(warningThrowable: Throwable?) {
        this.warningThrowable = warningThrowable
    }

    constructor(response: Response, warningThrowable: Throwable) {
        this.response = response
        this.warningThrowable = warningThrowable
    }

}