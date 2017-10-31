package com.rx.example.kotlintest001.model.http.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rx.example.kotlintest001.model.http.dao.Info
import com.rx.example.kotlintest001.model.http.dao.Result

/**
 */
public open class HttpRcvItemData
{
    @SerializedName("results")
    @Expose
    var results:  MutableList<Result>? = null
    @SerializedName("info")
    @Expose
    var info: Info? = Info("",0,0,"")

    constructor(results:  MutableList<Result>?, info: Info?) : super() {
        this.results = results
        this.info = info
    }

    constructor() : super()

}

