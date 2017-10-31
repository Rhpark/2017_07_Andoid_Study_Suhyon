package com.rx.example.kotlintest001.model.http.dao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Location :RealmObject
{
    @SerializedName("street")
    @Expose
    var street: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("state")
    @Expose
    var state: String? = null
    @SerializedName("postcode")
    @Expose
    var postcode: String? = null

    constructor(street: String?, city: String?, state: String?, postcode: String?) : super() {
        this.street = street
        this.city = city
        this.state = state
        this.postcode = postcode
    }

    constructor() : super()
}
