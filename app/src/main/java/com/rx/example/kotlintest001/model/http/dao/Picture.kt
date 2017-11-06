package com.rx.example.kotlintest001.model.http.dao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Picture(var large: String? = null
                          , var medium: String? = null
                          , var thumbnail: String? = null) : RealmObject()
{

}
