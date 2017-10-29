package com.rx.example.kotlintest001.network.http

import com.google.gson.Gson
import com.rx.example.kotlintest001.activity.presenter.ActivityRcvMainPresenter
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.network.HttpService
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.network.model.HttpRcvItemData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Rhpark on 2017-10-29.
 */
public class HttpRcvMain : HttpBase
{
    companion object {
        val DATA_SIZE = "5000" //data size 1 ~ 5000
    }

    var httpRcvItemData : HttpRcvItemData? = null

    constructor(httpJudgeListener: HttpJudgeListener?, netwrokController: NetworkController)
            : super(httpJudgeListener, netwrokController)

    override fun sendHttp() {

        //RxJava2 Single.
        http!!.getDatList(DATA_SIZE).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                if ( false == response!!.isSuccessful )
                {
                    httpJudgeListener!!.fail(RESPONE_FAIL)
                    return
                }

                try
                {
                    httpRcvItemData = Gson().fromJson(response.body()!!.string(), HttpRcvItemData::class.java)
                    httpJudgeListener!!.success(httpRcvItemData!!, RESPONE_SUCCESS)
                }
                catch (e: NullPointerException)
                {
                    Logger.e("Receive Data Error ",e.printStackTrace().toString())
                    httpJudgeListener!!.fail(RESPONE_DATA_ERROR)
                    return
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                httpJudgeListener!!.fail(RESPONE_ON_FAILURE)
            }
        })
    }
}