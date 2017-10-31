package com.rx.example.kotlintest001.network.http

import com.rx.example.kotlintest001.network.HttpService
import com.rx.example.kotlintest001.network.NetworkController

/**
 * Created by Rhpark on 2017-10-29.
 */
public abstract class HttpBase {

    open val RESPONE_FAIL       = "Response Fail"
    open val RESPONE_SUCCESS    = "Response isSuccessful"
    open val RESPONE_DATA_ERROR = "Response Data Error"
    open val RESPONE_ON_FAILURE = "Network onFailure"


    open var httpJudgeListener : HttpJudgeListener?
    open var http: HttpService?

    constructor(httpJudgeListener: HttpJudgeListener?, netwrokController: NetworkController) {
        this.httpJudgeListener = httpJudgeListener
        this.http = netwrokController.getHttpService(HttpService::class.java)
    }

    public open abstract fun sendHttp()

    open fun fail(msg:String) = httpJudgeListener!!.fail(msg)

    open fun success(hthtpData:Any, msg:String) = httpJudgeListener!!.success(hthtpData,msg)
}