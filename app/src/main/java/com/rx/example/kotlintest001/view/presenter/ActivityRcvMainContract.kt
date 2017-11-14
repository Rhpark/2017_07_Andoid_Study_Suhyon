package com.rx.example.kotlintest001.view.presenter

import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.model.http.dao.Result
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData

/**
 */
public interface ActivityRcvMainContract
{
    interface View
    {
        fun toastShow(msg: String)

        fun showProgressDialog(msg: String)

        fun dismissProgressDialog()
    }

    interface Presenter
    {
        var view: ActivityRcvMainContract.View

        fun onStartSendHttp(adapter: AdapterRcvMain)

        fun savedInstanceState(adapterRcvMain: AdapterRcvMain, adapterListItemCount: Int):Boolean

        fun sendHttpRetry(dataSize: Int)

        fun isCheckNetworkState(): Boolean

        fun listener(adapter: AdapterRcvMain)

        fun onDestroy()
    }

    interface Model
    {
        fun saveDataSendHttpSuccess(httpRcvItemData: HttpRcvItemData)

        fun loadAllData()

        fun getDataSize():Int

        fun getHttpData():HttpRcvItemData

        fun getHttpResults():MutableList<Result>

        fun isGetResultData():Boolean

        fun onDestroy()
    }
}