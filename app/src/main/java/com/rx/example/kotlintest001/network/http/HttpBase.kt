package com.rx.example.kotlintest001.network.http

import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.network.HttpService
import com.rx.example.kotlintest001.network.NetworkController
import io.reactivex.subjects.PublishSubject

/**
 * Created by Rhpark on 2017-10-29.
 */
public abstract class HttpBase
{
    open val RESPONE_FAIL       = "Response Fail"
    open val RESPONE_SUCCESS    = "Response isSuccessful"
    open val RESPONE_DATA_ERROR = "Response Data Error"
    open val RESPONE_ON_FAILURE = "Network onFailure"

    open val rxPsSuccess: PublishSubject<Any>
    open val rxPsFail: PublishSubject<String>
    open val httpService: HttpService

    open var startTime = 0L
    var responseTime = 0L

    constructor(networkController: NetworkController)
    {
        this.rxPsSuccess = PublishSubject.create()
        this.rxPsFail = PublishSubject.create()
        this.httpService = networkController.getHttpService(HttpService::class.java)
    }

    abstract fun sendHttpList(dataSize:Int)

    open fun fail(msg:String)
    {
        responseTime = System.currentTimeMillis()
        rxPsFail.onNext(msg)
    }

    open fun responseSuccess(httpData: Any)
    {
        responseTime = System.currentTimeMillis()
        rxPsSuccess.onNext(httpData)
    }

    open fun onResponseFinish(className : String)
    {
        Logger.d(className,"ResponseTime " + (responseTime - startTime))
        System.gc()
    }
}