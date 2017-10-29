package com.rx.example.kotlintest001.model.http

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 */

public data class Login ( val username: String,  val password: String, val salt: String,
                          val md5: String, val sha1: String, val sha256: String)
{

}
