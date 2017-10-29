package com.rx.example.kotlintest001.model.http

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.HashMap

/**
 */

public data class Info (val seed: String,val results: Int, val page: Int, val version: String) {

}
