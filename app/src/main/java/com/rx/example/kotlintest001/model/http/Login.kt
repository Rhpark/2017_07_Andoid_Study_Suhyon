package com.rx.example.kotlintest001.model.http

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Login :RealmObject
{
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("salt")
    @Expose
    var salt: String? = null
    @SerializedName("md5")
    @Expose
    var md5: String? = null
    @SerializedName("sha1")
    @Expose
    var sha1: String? = null
    @SerializedName("sha256")
    @Expose
    var sha256: String? = null

    constructor(username: String?, password: String?, salt: String?, md5: String?, sha1: String?, sha256: String?) : super() {
        this.username = username
        this.password = password
        this.salt = salt
        this.md5 = md5
        this.sha1 = sha1
        this.sha256 = sha256
    }

    constructor() : super()


}
