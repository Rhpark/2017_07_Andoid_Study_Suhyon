package com.rx.example.kotlintest001.model.http.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Picture(var large: String = "TempLarge"
                          , var medium: String = "TempMedium"
                          , var thumbnail: String = "TempThumbnail") : RealmObject()
{

}
