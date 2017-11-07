package com.rx.example.kotlintest001.view.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.model.ModelRcvMain
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.network.http.HttpRcvMain
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg
import io.reactivex.subjects.PublishSubject


class ActivityRcvMainPresenter : ActivityRcvMainContract.Presenter
{
    override var view: ActivityRcvMainContract.View

    private var networkController: NetworkController

    private var modelRcvMain:ModelRcvMain

    private var httpRcvmain:HttpRcvMain

    constructor(view: ActivityRcvMainContract.View,context:Context)
    {
        this.view = view
        modelRcvMain = ModelRcvMain(context)
        networkController = NetworkController(context)
        httpRcvmain = HttpRcvMain(networkController)
    }

    override fun sendHttpSuccess(gsonConvertData: HttpRcvItemData, adapterRcvMain: AdapterRcvMain)
    {
        rcvAdapterUpdate(gsonConvertData, adapterRcvMain)

        modelRcvMain.saveDataSendHttpSuccess(gsonConvertData)

        view.dismissProgressDialog()
    }

    override fun sendHttpFail(msg: String,adapterRcvMain: AdapterRcvMain)
    {
        view.toastShow(msg)
        if ( modelRcvMain.isGetResultData())
        {
            modelRcvMain.saveDataSendHttpFail()
            rcvAdapterUpdate(modelRcvMain.getHttpData(), adapterRcvMain)
            view.toastShow("Load data, from realm")
        }

        view.dismissProgressDialog()
    }

    private fun rcvAdapterUpdate(httpRcvItemData: HttpRcvItemData, adapterRcvMain: AdapterRcvMain)
    {
        adapterRcvMain.results = httpRcvItemData.results!!
        adapterRcvMain.addItemListSize()
        adapterRcvMain.notifyDataSetChanged()
    }

    override fun onStartSendHttp()
    {
        if ( modelRcvMain.isGetResultData())
        {
            modelRcvMain.loadAllData()
            sendHttp(modelRcvMain.getDataSize())
        }
        else
            sendHttp(5000)
    }

    private fun sendHttp(dataSize:Int)
    {
        if ( true == networkController.isNetworkCheck())
        {
            view.setClickBtn(false)
            view.showProgressDialog("통신 중 입니다.\n Data Size " + dataSize)
            httpRcvmain.sendHttpList(dataSize)
        }
        else
            view.toastShow("Please check, Network state")
    }

    override fun sendHttpRetry(dataSize:Int) = sendHttp(dataSize)

    override fun rcvMoveToPosition(rcvMain: RecyclerView, currentPosition:Int)
    {
        rcvMain.smoothScrollToPosition(currentPosition )
        view.dismissProgressDialog()
    }

    override fun rcvShowAddValue(adapterRcvMain: AdapterRcvMain) : Int
    {
        view.showProgressDialog("add Data..")

        val defSize   = modelRcvMain.getHttpResults().size - adapterRcvMain.listSize

        var moveToPosition = AdapterRcvMain.MAX_ADD_VALUE

        if (defSize <  moveToPosition)
            moveToPosition = defSize

        adapterRcvMain.listSize += moveToPosition

        adapterRcvMain.notifyDataSetChanged()

        return moveToPosition
    }

    override fun isCheckAdapterItemSizeAdd(currentPosition: Int, itemCount:Int):Boolean
        = ( true == isCheckRcvScrollBottom(currentPosition, itemCount)
            && true == modelRcvMain.isRcvAddSizeUp( itemCount ))

    private fun isCheckRcvScrollBottom(currentPosition: Int, itemCount:Int): Boolean
            = ( currentPosition == itemCount-1 )

    override fun isCheckRetry(ceDlgRetry : AlertEditDlg):Boolean
    {
        ceDlgRetry.closeKeyboard()

        if ( ceDlgRetry.isGetNumber())
        {
            val getValue = ceDlgRetry.getNumber()
            if ( getValue  < 1 || getValue > 5000)
            {
                view.toastShow("Can not Change Data Size\n please Input Number Size 1~5000")
                return false
            }
            return true
        }
        else
        {
            view.toastShow("Can not Change Data Size\n please Input number")
            return false
        }
    }

    override fun isCheckSearchDlgBtnOk(ceDlgRetry : AlertEditDlg): Boolean
    {
        ceDlgRetry.closeKeyboard()

        if ( false == networkController.isNetworkCheck())
        {
            view.toastShow("Please check, Network state")
            return false
        }

        if (false ==  ceDlgRetry.isGetNumber())
        {
            view.toastShow("Can not Change Data Size\n please Input number")
            return false
        }

        val getValue = ceDlgRetry.getNumber()
        if ( getValue  < 0 || getValue > (getDataSize()-1))
        {
            view.toastShow("Can not Change Data Size\n please Input Number Size 1 ~ " + (getDataSize()-1))
            return false
        }
        view.toastShow("find Data!")
        return true
    }

    override fun httpListenerSuccess(): PublishSubject<HttpRcvItemData> = httpRcvmain.psSuccess

    override fun httpListenerFail(): PublishSubject<String> = httpRcvmain.psFail

    override fun getDataSize():Int = modelRcvMain.getDataSize()

    override fun getRealmIsInserted(): PublishSubject<Boolean> = modelRcvMain.realmHttpRcvDTO.psRealmlIsInserted

    override fun onDestroy()
    {
        modelRcvMain.onDestroy()
    }
}
