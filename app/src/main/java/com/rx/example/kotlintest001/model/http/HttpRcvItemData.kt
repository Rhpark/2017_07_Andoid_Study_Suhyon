package com.rx.example.kotlintest001.model.http

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 */
public open class HttpRcvItemData
{
    @SerializedName("results")
    @Expose
    var results:  MutableList<Result>? = null
    @SerializedName("info")
    @Expose
    var info: Info? = null

    constructor(results:  MutableList<Result>?, info: Info?) : super() {
        this.results = results
        this.info = info
    }

    constructor() : super()

}

