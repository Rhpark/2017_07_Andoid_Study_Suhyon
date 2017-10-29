package com.rx.example.kotlintest001.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 */

public data class Name (val title: String, val first: String, val last: String)
{
    public fun getFullName():String = (last + " " + first)
}
