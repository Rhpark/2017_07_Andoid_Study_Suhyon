package com.rx.example.kotlintest001.model.http.dto

import com.rx.example.kotlintest001.model.http.dto.*
import io.realm.RealmObject

/**
 */

open class Result(var gender: String = "TempGender"
                  , var name: Name = Name()
                  , var location: Location = Location()
                  , var email: String = "TempEmail"
                  , var login: Login = Login()
                  , var dob: String = "TempDob"
                  , var registered: String = "TempRegstered"
                  , var phone: String = "TempPhone"
                  , var cell: String = "TempCell"
                  , var id: Id = Id()
                  , var picture: Picture? = Picture()
                  , var nat: String = "TempNat") :RealmObject()
{

}
