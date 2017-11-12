package com.rx.example.kotlintest001.model.http.dto

/**
 */
data class HttpRcvItemData(var results:  MutableList<Result>? = null
                            , var info: Info? = Info("", 0, 0, ""))
{

}

