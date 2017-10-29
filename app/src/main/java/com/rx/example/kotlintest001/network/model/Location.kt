package com.rx.example.kotlintest001.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 */

public data class Location (val street: String, val city: String, val state: String, val postcode: String)
{

}
