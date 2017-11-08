package com.rx.example.kotlintest001.view.presenter

import android.content.Context
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.model.ModelRcvMain
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.network.http.HttpRcvMain
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg
import io.reactivex.disposables.Disposable
import kotlin.properties.Delegates


class ActivityRcvMainPresenter : ActivityRcvMainContract.Presenter
{
    override var view: ActivityRcvMainContract.View

    private var networkController: NetworkController

    private var modelRcvMain:ModelRcvMain

    private var httpRcvmain:HttpRcvMain

    private var dspbHttpSuccess:        Disposable by Delegates.notNull()   //CallBack Http Success
    private var dspbHttpFail:           Disposable by Delegates.notNull()   //CallBack Http Fail
    private var dspbRealmIsInserted:    Disposable by Delegates.notNull()   //CallBack Realm Save

    constructor(view: ActivityRcvMainContract.View,context:Context)
    {
        this.view = view
        modelRcvMain = ModelRcvMain(context)
        networkController = NetworkController(context)
        httpRcvmain = HttpRcvMain(networkController)
    }

    fun sendHttpSuccess(httpRcvItemData: HttpRcvItemData, adapterRcvMain: AdapterRcvMain)
    {
        rcvAdapterUpdate(httpRcvItemData, adapterRcvMain)

        view.dismissProgressDialog()
        modelRcvMain.saveDataSendHttpSuccess(httpRcvItemData)
    }

    fun sendHttpFail(msg: String,adapterRcvMain: AdapterRcvMain)
    {
        view.toastShow(msg)

        if ( true == adapterUpdate(adapterRcvMain))
            view.toastShow("Load data, from realm")

        view.dismissProgressDialog()
    }

    fun adapterUpdate(adapterRcvMain: AdapterRcvMain):Boolean {

        if ( modelRcvMain.isGetResultData())
        {
            modelRcvMain.loadAllData()
            rcvAdapterUpdate(modelRcvMain.getHttpData(), adapterRcvMain)
            return true
        }
        return false
    }

    fun savedInstanceState(adapterRcvMain: AdapterRcvMain, adapterListItemCount: Int):Boolean {

        if ( adapterUpdate( adapterRcvMain ))
        {
            adapterRcvMain.listSize = adapterListItemCount
            adapterRcvMain.notifyDataSetChanged()
            return true
        }
        return false
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
            view.showProgressDialog("통신 중 입니다.\n Data Size " + dataSize)
            httpRcvmain.sendHttpList(dataSize)
        }
        else
            view.toastShow("Please check, Network state")
    }

    override fun sendHttpRetry(dataSize:Int) = sendHttp(dataSize)

    override fun listener(adapter:AdapterRcvMain)
    {
        //httpCallBack
        dspbHttpSuccess = httpRcvmain.psSuccess.subscribe { sendHttpSuccess(it, adapter) }

        dspbHttpFail = httpRcvmain.psFail.subscribe { sendHttpFail(it, adapter) }

        dspbRealmIsInserted = modelRcvMain.realmHttpRcvDTO.psRealmlIsInserted
                .subscribe {
                    if ( it  == false)  view.toastShow("Data Save Fail")
                }
    }

    private fun rcvAdapterUpdate(httpRcvItemData: HttpRcvItemData, adapterRcvMain: AdapterRcvMain)
    {
        adapterRcvMain.results = httpRcvItemData.results!!
        adapterRcvMain.addItemListSize()
        adapterRcvMain.notifyDataSetChanged()
    }

    override fun rcvShowAddValue(adapterRcvMain: AdapterRcvMain) : Int
    {
        view.showProgressDialog("add Data..")

        val defSize   = adapterRcvMain.results.size - adapterRcvMain.listSize

        var moveToPosition = AdapterRcvMain.MAX_ADD_VALUE

        if (defSize <  moveToPosition)
            moveToPosition = defSize

        adapterRcvMain.listSize += moveToPosition

        adapterRcvMain.notifyDataSetChanged()

        return ( adapterRcvMain.listSize - (moveToPosition-1))
    }

    override fun isCheckAdapterItemSizeAdd(currentPosition: Int, itemCount:Int, totalDataSize:Int):Boolean
        = ( true == isCheckRcvScrollBottom(currentPosition, itemCount)
            && true == isRcvAddSizeUp( itemCount,totalDataSize ))

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

    override fun isCheckSearchDlgBtnOk(ceDlgRetry : AlertEditDlg, dataSize:Int): Boolean
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

        if ( getValue  < 0 || getValue > dataSize)
        {
            view.toastShow("Can not Change Data Size\n please Input Number Size 1 ~ " + (dataSize - 1) )
            return false
        }
        view.toastShow("find Data!")
        return true
    }

    private fun isRcvAddSizeUp(lastSize: Int, resultsDataSize:Int): Boolean
    {
        val defSize   = resultsDataSize - lastSize

        if ( resultsDataSize <= lastSize || resultsDataSize <= defSize)
            return false

        return true
    }

    override fun onDestroy()
    {
        dspbHttpSuccess.dispose()
        dspbHttpFail.dispose()
        dspbRealmIsInserted.dispose()
        modelRcvMain.onDestroy()
    }
}
