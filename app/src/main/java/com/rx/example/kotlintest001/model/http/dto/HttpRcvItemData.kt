package com.rx.example.kotlintest001.model.http.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rx.example.kotlintest001.model.http.dao.Info
import com.rx.example.kotlintest001.model.http.dao.Result

/**
 */
data class HttpRcvItemData(var results:  MutableList<Result>? = null
                            , var info: Info? = Info("",0,0,""))
{

}

