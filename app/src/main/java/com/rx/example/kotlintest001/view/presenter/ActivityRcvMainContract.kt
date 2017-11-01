package com.rx.example.kotlintest001.view.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.model.http.dao.Result
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.network.http.HttpJudgeListener
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg

/**
 */
public interface ActivityRcvMainContract {

    interface View
    {
        fun toastShow(msg:String)

        fun showProgressDialog(msg:String)

        fun dismissProgressDialog()
    }

    interface Presenter
    {
        var view: ActivityRcvMainContract.View

        fun sendHttp(httpJudgeListener: HttpJudgeListener)

        fun sendHttpSuccess(gsonConvertData: HttpRcvItemData, msg: String, adapterRcvMain: AdapterRcvMain)

        fun sendHttpFail(msg: String,adapterRcvMain: AdapterRcvMain)

        fun isCheckRetry(ceDlgRetry : AlertEditDlg):Boolean

        fun getDataSize():Int

        fun rcvShowAddValue(adapterRcvMain: AdapterRcvMain):Int

        fun rcvMoveToPosition(rcvMain: RecyclerView, currentPosition:Int)

        fun isCheckAdapterItemSizeAdd(currentPosition: Int, itemCount:Int):Boolean

        fun sendHttpRetry(httpJudgeListener: HttpJudgeListener, dataSize:Int)

        fun isCheckSearchDlgBtnOk(ceDlgRetry : AlertEditDlg): Boolean

        fun onDestroy()
    }

    interface Model
    {
        fun saveDataSendHttpSuccess(httpRcvItemData:HttpRcvItemData)

        fun saveDataSendHttpFail()

        fun saveHttpDataSize(dataSize:Int)

        fun getDataSize():Int

        fun getHttpData():HttpRcvItemData

        fun getHttpResults():MutableList<Result>

        fun isGetResultData():Boolean

        fun isRcvAddSizeUp(lastSize: Int):Boolean

        fun onDestroy()
    }
}