package com.rx.example.kotlintest001.model.http

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Id :RealmObject {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null

    public constructor(name: String?, value: String?) : super() {
        this.name = name
        this.value = value
    }

    public constructor() : super()
}

