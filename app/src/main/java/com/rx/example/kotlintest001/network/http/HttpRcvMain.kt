package com.rx.example.kotlintest001.network.http

import com.google.gson.Gson
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.model.http.HttpRcvItemData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Rhpark on 2017-10-29.
 */
public class HttpRcvMain : HttpBase
{
    var httpRcvItemData : HttpRcvItemData? = null
    var dataSize = 0
    constructor(httpJudgeListener: HttpJudgeListener?, netwrokController: NetworkController, dataSize:Int)
            : super(httpJudgeListener, netwrokController)
    {
        this.dataSize = dataSize
    }



    override fun sendHttp() {

        //RxJava2 Single.
        http!!.getDatList(dataSize).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                if ( false == response!!.isSuccessful )
                {
                    fail(RESPONE_FAIL)
                    return
                }

                try
                {
                    httpRcvItemData = Gson().fromJson(response.body()!!.string(), HttpRcvItemData::class.java)
                    success(httpRcvItemData!!, RESPONE_SUCCESS)
                }
                catch (e: NullPointerException)
                {
                    Logger.e("Receive Data Error ",e.printStackTrace().toString())
                    fail(RESPONE_DATA_ERROR)
                    return
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
               fail(RESPONE_ON_FAILURE)
            }
        })
    }
}