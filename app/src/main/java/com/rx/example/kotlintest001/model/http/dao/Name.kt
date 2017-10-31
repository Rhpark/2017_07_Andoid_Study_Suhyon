package com.rx.example.kotlintest001.model.http.dao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Name : RealmObject {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("first")
    @Expose
    var first: String? = null
    @SerializedName("last")
    @Expose
    var last: String? = null

    public constructor(title: String?, first: String?, last: String?) {
        this.title = title
        this.first = first
        this.last = last
    }

    public constructor()


    fun fullName():String = "$first $last"
}