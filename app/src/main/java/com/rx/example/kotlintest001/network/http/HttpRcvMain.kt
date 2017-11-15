package com.rx.example.kotlintest001.network.http

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
    constructor(networkController: NetworkController): super(networkController)

    override fun sendHttpList(dataSize:Int)
    {
        startTime = System.currentTimeMillis()

        //RxJava2 Single.
        httpService.getDatList(dataSize).enqueue(object : Callback<HttpRcvItemData> {
            override fun onResponse(call: Call<HttpRcvItemData>?, response: Response<HttpRcvItemData>?)
            {
                response?.let {

                    if (false == response.isSuccessful)
                        fail(RESPONE_FAIL)
                    else
                        it.body()?.let { responseSuccess(it) }

                } ?: fail(RESPONE_DATA_ERROR)


                /*** 주석과 같은 의미 ***/
//                if (response == null)
//                    fail(RESPONE_DATA_ERROR)
//                else if ( false == response.isSuccessful )
//                    successFullFail()
//                else
//                {
//                    response?.body()?.let {
//                        success(it)
//                    }
//                }

                onResponseFinish("HttpRcvMain")
            }

            override fun onFailure(call: Call<HttpRcvItemData>?, t: Throwable?)
            {
                fail(RESPONE_ON_FAILURE)
            }
        })
    }
}