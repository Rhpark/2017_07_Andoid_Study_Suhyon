package com.rx.example.kotlintest001.model.http.dao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */
open class Id(var name: String? = "TempName"
              , var value: String? = "TempValue"): RealmObject()
{

}

