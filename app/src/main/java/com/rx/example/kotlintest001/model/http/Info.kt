package com.rx.example.kotlintest001.model.http

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Info :RealmObject {


    @SerializedName("seed")
    @Expose
    var seed: String? = null
    @SerializedName("results")
    @Expose
    var results: Int? = null
    @SerializedName("page")
    @Expose
    var page: Int? = null
    @SerializedName("version")
    @Expose
    var version: String? = null

    constructor(seed: String?, results: Int?, page: Int?, version: String?) : super() {
        this.seed = seed
        this.results = results
        this.page = page
        this.version = version
    }

    constructor() : super()
}
