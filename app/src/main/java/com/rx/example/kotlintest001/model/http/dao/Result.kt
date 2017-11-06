package com.rx.example.kotlintest001.model.http.dao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

open class Result(var gender: String? = "TempGender"
                  , var name: Name? = null
                  , var location: Location? = null
                  , var email: String? = "TempEmail"
                  , var login: Login? = null
                  , var dob: String? = "TempDob"
                  , var registered: String? = "TempRegstered"
                  , var phone: String? = "TempPhone"
                  , var cell: String? = "TempCell"
                  , var id: Id? = null
                  , var picture: Picture? = null
                  , var nat: String? = null) :RealmObject()
{

}
