package com.rx.example.kotlintest001.model.http.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 */

public open class Login(var username: String? = "TempUsername"
                        , var password: String? = "TempPassword"
                        , var salt: String? = "TempSalt"
                        , var md5: String? = "TempMd5"
                        , var sha1: String? = "TempSha1"
                        , var sha256: String? = "TempSha256") :RealmObject()
{

}
