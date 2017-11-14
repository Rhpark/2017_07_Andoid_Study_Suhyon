package com.rx.example.kotlintest001.network.http

import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.model.http.dao.HttpRcvItemData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Rhpark on 2017-10-29.
 */
public class HttpRcvMain : HttpBase
{
    var startTime = 0L
    var successTime = 0L
    var endTime = 0L

    constructor(networkController: NetworkController): super(networkController)

    override fun sendHttpList(dataSize:Int)
    {
        startTime = System.currentTimeMillis()

        //RxJava2 Single.
        http.getDatList(dataSize).enqueue(object : Callback<HttpRcvItemData> {
            override fun onResponse(call: Call<HttpRcvItemData>?, response: Response<HttpRcvItemData>?)
            {
                response?.let {
                    if ( false == response.isSuccessful )
                    {
                        Logger.e("isSuccessful Error ")
                        fail(RESPONE_FAIL)
                        successTime = 0L
                    }
                    try
                    {
                        successTime = System.currentTimeMillis()
                        response.body()?.let { success(it) }

                    }
                    catch (e: NullPointerException)
                    {
                        Logger.e("Receive Data Error ", e.printStackTrace().toString() )
                        fail(RESPONE_DATA_ERROR)
                    }
                }
                endTime = System.currentTimeMillis()
                Logger.d("success Time " + (successTime - startTime) + " , end time " + (endTime - startTime) )
                System.gc()
            }

            override fun onFailure(call: Call<HttpRcvItemData>?, t: Throwable?)
            {
                fail(RESPONE_ON_FAILURE)
            }
        })
    }
}