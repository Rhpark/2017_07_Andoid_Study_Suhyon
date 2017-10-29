package com.rx.example.kotlintest001.network.http

import okhttp3.ResponseBody
import retrofit2.Response

/**
 * Created by Rhpark on 2017-10-29.
 */
public interface HttpJudgeListener {

    public fun success(gsonConvertData: Any, msg:String)
    public fun fail(msg:String)
}