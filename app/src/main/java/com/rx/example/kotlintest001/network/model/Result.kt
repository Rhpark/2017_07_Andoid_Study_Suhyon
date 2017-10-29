package com.rx.example.kotlintest001.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.HashMap

/**
 */

public data class Result (val gender: String, val name: Name, val location: Location,
                          val email: String, val login: Login, val dob: String,
                          val registered: String, val phone: String, val cell: String,
                          val id: Id, val picture: Picture, val nat: String)
{

}
