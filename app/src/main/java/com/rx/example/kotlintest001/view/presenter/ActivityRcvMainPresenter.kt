package com.rx.example.kotlintest001.view.presenter

import android.content.Context
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.model.ModelRcvMain
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.network.http.HttpRcvMain
import com.rx.example.kotlintest001.model.http.dao.HttpRcvItemData
import io.reactivex.disposables.Disposable

class ActivityRcvMainPresenter : ActivityRcvMainContract.Presenter
{
    val DefaultMaxDataSize = 5000

    override var view: ActivityRcvMainContract.View

    private var networkController: NetworkController

    private var modelRcvMain:ModelRcvMain

    private var httpRcvmain:HttpRcvMain
    private lateinit var dspbHttpSuccess: Disposable        //CallBack Http Success
    private lateinit var dspbHttpFail: Disposable           //CallBack Http Fail
    private lateinit var dspbRealmIsInserted: Disposable    //CallBack Realm Save

    constructor(view: ActivityRcvMainContract.View,context:Context)
    {
        this.view = view
        modelRcvMain = ModelRcvMain(context)
        networkController = NetworkController(context)
        httpRcvmain = HttpRcvMain(networkController)
    }

    private fun sendHttpSuccess(httpRcvItemData: HttpRcvItemData, adapterRcvMain: AdapterRcvMain)
    {
        rcvAdapterUpdate(httpRcvItemData, adapterRcvMain)

        view.dismissProgressDialog()
        modelRcvMain.saveDataSendHttpSuccess(httpRcvItemData)
    }

    private fun sendHttpFail(msg: String,adapterRcvMain: AdapterRcvMain)
    {
        view.toastShow(msg)

        if ( true == adapterUpdateFromRealm(adapterRcvMain))
            view.toastShow("Load data, from realm")

        view.dismissProgressDialog()
    }

    private fun adapterUpdateFromRealm(adapterRcvMain: AdapterRcvMain):Boolean {

        if ( modelRcvMain.isGetResultData())
        {
            modelRcvMain.loadAllData()
            rcvAdapterUpdate(modelRcvMain.getHttpData(), adapterRcvMain)
            return true
        }
        return false
    }

    override fun savedInstanceState(adapterRcvMain: AdapterRcvMain, adapterListItemCount: Int):Boolean {

        if ( adapterUpdateFromRealm( adapterRcvMain ))
        {
            adapterRcvMain.listSize = adapterListItemCount
            adapterRcvMain.notifyDataSetChanged()
            return true
        }
        return false
    }

    override fun onStartSendHttp(adapter: AdapterRcvMain)
    {
        if ( false == isCheckNetworkState() || modelRcvMain.isGetResultData())
            adapterUpdateFromRealm(adapter)

        else
            sendHttp(DefaultMaxDataSize)
    }

    private fun sendHttp(dataSize:Int)
    {
        if ( false == networkController.isNetworkCheck())
            view.toastShow("Please check, Network state")

        else
        {
            view.showProgressDialog("통신 중 입니다.\n Data Size " + dataSize)
            httpRcvmain.sendHttpList(dataSize)
        }
    }

    override fun sendHttpRetry(dataSize:Int)
    {
        if ( dataSize  < 1 || dataSize > DefaultMaxDataSize)
            view.toastShow("Can not Change Data Size\n please Input Number Size 1 ~ " + DefaultMaxDataSize)

        else
            sendHttp(dataSize)
    }

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

    private fun rcvAdapterUpdate(httpRcvItemData: HttpRcvItemData?, adapterRcvMain: AdapterRcvMain)
    {
        httpRcvItemData?.let {
            adapterRcvMain.results = it.results
            adapterRcvMain.addItemListSize()
            adapterRcvMain.notifyDataSetChanged()
        }
    }

    override fun isCheckNetworkState(): Boolean
    {

        if ( false == networkController.isNetworkCheck())
        {
            view.toastShow("Please check, Network state")
            return false
        }
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
