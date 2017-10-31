package com.rx.example.kotlintest001.view.presenter

import android.support.v7.widget.RecyclerView
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.network.http.HttpJudgeListener
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg

/**
 */
public interface ActivityRcvMainPresenterImp{

    fun sendHttp(httpJudgeListener: HttpJudgeListener)

    fun rcvShowAddValue(rcvMain: RecyclerView, adapterRcvMain: AdapterRcvMain):Int

    fun sendHttpSuccess(gsonConvertData: Any, msg: String,rcvMain: RecyclerView,adapterRcvMain: AdapterRcvMain)

    fun sendHttpFail(msg: String,rcvMain: RecyclerView,adapterRcvMain: AdapterRcvMain)

    fun rcvMoveToPosition(rcvMain: RecyclerView, currentPosition:Int)

    fun isRcvAddValue(adapterRcvMain: AdapterRcvMain):Boolean

    fun isCheckRetry(ceDlgRetry : AlertEditDlg):Boolean

    fun clickBtnOkRetry(ceDlgRetry : AlertEditDlg)

    fun clickSearchDlgBtnOk(ceDlgRetry : AlertEditDlg)

    fun getDataSize():Int

    fun onDestroy()
}