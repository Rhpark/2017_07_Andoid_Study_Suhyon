package com.rx.example.kotlintest001.view.presenter

import android.support.v7.widget.RecyclerView
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.model.http.dao.Result
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg
import io.reactivex.subjects.PublishSubject

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

        fun onStartSendHttp()

        fun isCheckRetry(ceDlgRetry : AlertEditDlg):Boolean

        fun rcvShowAddValue(adapterRcvMain: AdapterRcvMain):Int

        fun isCheckAdapterItemSizeAdd(currentPosition: Int, itemCount:Int):Boolean

        fun sendHttpRetry(dataSize:Int)

        fun isCheckSearchDlgBtnOk(ceDlgRetry : AlertEditDlg): Boolean

        fun listener(adapter:AdapterRcvMain)

        fun isBtnClick():Boolean

        fun onDestroy()
    }

    interface Model
    {
        fun saveDataSendHttpSuccess(httpRcvItemData:HttpRcvItemData)

        fun saveDataSendHttpFail()

        fun getDataSize():Int

        fun getHttpData():HttpRcvItemData

        fun getHttpResults():MutableList<Result>

        fun isGetResultData():Boolean

        fun isRcvAddSizeUp(lastSize: Int):Boolean

        fun onDestroy()
    }
}