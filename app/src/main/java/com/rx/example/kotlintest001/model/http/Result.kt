package com.rx.example.kotlintest001.model.http

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Result :RealmObject {

    @SerializedName("gender")
    @Expose
    var gender: String? = null
    @SerializedName("name")
    @Expose
    var name: Name? = null
    @SerializedName("location")
    @Expose
    var location: Location? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("login")
    @Expose
    var login: Login? = null
    @SerializedName("dob")
    @Expose
    var dob: String? = null
    @SerializedName("registered")
    @Expose
    var registered: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("cell")
    @Expose
    var cell: String? = null
    @SerializedName("id")
    @Expose
    var id: Id? = null
    @SerializedName("picture")
    @Expose
    var picture: Picture? = null
    @SerializedName("nat")
    @Expose
    var nat: String? = null


    public constructor() : super()
    constructor(gender: String?, name: Name?, location: Location?, email: String?, login: Login?,
                dob: String?, registered: String?, phone: String?, cell: String?, id: Id?,
                picture: Picture?, nat: String?) : super() {
        this.gender = gender
        this.name = name
        this.location = location
        this.email = email
        this.login = login
        this.dob = dob
        this.registered = registered
        this.phone = phone
        this.cell = cell
        this.id = id
        this.picture = picture
        this.nat = nat
    }
}
