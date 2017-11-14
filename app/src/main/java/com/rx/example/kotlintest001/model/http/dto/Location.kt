package com.rx.example.kotlintest001.model.http.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Location( var street: String? = "TempStreet"
                            , var city: String? = "TempCity"
                            , var state: String? = "TempState"
                            , var postcode: String? = "TempPostcode") : RealmObject()
{

}
