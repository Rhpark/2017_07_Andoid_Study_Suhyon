package com.rx.example.kotlintest001.network.http

import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Rhpark on 2017-10-29.
 */
public class HttpRcvMain : HttpBase
{
    constructor(networkController: NetworkController): super(networkController)

    override fun sendHttpList(dataSize:Int) {

        //RxJava2 Single.
        http.getDatList(dataSize).enqueue(object : Callback<HttpRcvItemData> {
            override fun onResponse(call: Call<HttpRcvItemData>?, response: Response<HttpRcvItemData>?)
            {
                if ( false == response!!.isSuccessful )
                {
                    fail(RESPONE_FAIL)
                    return
                }
                try
                {
                    success(response.body()!!)
                }
                catch (e: NullPointerException)
                {
                    Logger.e("Receive Data Error ", e.printStackTrace().toString())
                    fail(RESPONE_DATA_ERROR)
                    return
                }
            }

            override fun onFailure(call: Call<HttpRcvItemData>?, t: Throwable?)
            {
                fail(RESPONE_ON_FAILURE)
            }
        })
    }
}