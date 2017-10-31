package com.rx.example.kotlintest001.model.http.dao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Picture : RealmObject
{
    @SerializedName("large")
    @Expose
    var large: String? = null
    @SerializedName("medium")
    @Expose
    var medium: String? = null
    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null

    constructor(large: String?, medium: String?, thumbnail: String?) : super() {
        this.large = large
        this.medium = medium
        this.thumbnail = thumbnail
    }

    constructor() : super()

}
