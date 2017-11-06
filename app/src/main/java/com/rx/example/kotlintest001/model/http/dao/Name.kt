package com.rx.example.kotlintest001.model.http.dao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Name(var title: String? = "TempTitle"
                       ,var first: String? = "TempFirst"
                       ,var last: String? = "TempLast"): RealmObject()
{
    fun fullName():String = "$first $last"
}