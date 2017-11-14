package com.rx.example.kotlintest001.model.http.dao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

open class Info(var seed: String? = "TempSeed"
                , var results: Int? = -9999
                , var page: Int? = -9999
                , var version: String? = " TempVersion") : io.realm.RealmObject()
{

}
