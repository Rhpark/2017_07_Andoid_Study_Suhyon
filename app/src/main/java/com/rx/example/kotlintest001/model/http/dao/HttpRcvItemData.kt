package com.rx.example.kotlintest001.model.http.dao

import com.rx.example.kotlintest001.model.http.dto.Info
import com.rx.example.kotlintest001.model.http.dto.Result

/**
 */
data class HttpRcvItemData(var results:  MutableList<Result> = mutableListOf(Result())
                            , var info: Info? = Info())
{

}

