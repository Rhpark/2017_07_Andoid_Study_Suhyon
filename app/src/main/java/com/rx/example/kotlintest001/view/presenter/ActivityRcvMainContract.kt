package com.rx.example.kotlintest001.view.presenter

import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.model.http.dto.Result
import com.rx.example.kotlintest001.model.http.dao.HttpRcvItemData
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg

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

        fun onStartSendHttp()

        fun isCheckRetry(ceDlgRetry : AlertEditDlg):Boolean

        fun rcvShowAddValue(adapterRcvMain: AdapterRcvMain):Int

        fun isCheckAdapterItemSizeAdd(currentPosition: Int, itemCount:Int, totalDataSize:Int):Boolean

        fun sendHttpRetry(dataSize: Int)

        fun isCheckSearchDlgBtnOk(ceDlgRetry : AlertEditDlg, dataSize: Int): Boolean

        fun listener(adapter: AdapterRcvMain)

        fun onDestroy()
    }

    interface Model
    {
        fun saveDataSendHttpSuccess(httpRcvItemData: HttpRcvItemData)

        fun loadAllData()

        fun getDataSize():Int

        fun getHttpData(): HttpRcvItemData

        fun getHttpResults():MutableList<Result>

        fun isGetResultData():Boolean

        fun onDestroy()
    }
}