package com.rx.example.kotlintest001.network.http

import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.network.HttpService
import com.rx.example.kotlintest001.network.NetworkController
import io.reactivex.subjects.PublishSubject

/**
 * Created by Rhpark on 2017-10-29.
 */
public abstract class HttpBase {

    open val RESPONE_FAIL       = "Response Fail"
    open val RESPONE_SUCCESS    = "Response isSuccessful"
    open val RESPONE_DATA_ERROR = "Response Data Error"
    open val RESPONE_ON_FAILURE = "Network onFailure"

    open val psSuccess: PublishSubject<HttpRcvItemData>
    open val psFail: PublishSubject<String>
    open val http: HttpService

    constructor( netwrokController: NetworkController) {
        this.psSuccess = PublishSubject.create()
        this.psFail = PublishSubject.create()
        this.http = netwrokController.getHttpService(HttpService::class.java)
    }

    abstract fun sendHttpList(dataSize:Int)

    open fun fail(msg:String) = psFail.onNext(msg)

    open fun success(httpData:HttpRcvItemData) = psSuccess.onNext(httpData)
}