package com.rx.example.kotlintest001.view.presenter

import android.support.v7.widget.RecyclerView
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.model.ModelRcvMain
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.network.http.HttpJudgeListener
import com.rx.example.kotlintest001.network.http.HttpRcvMain
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg


class ActivityRcvMainPresenter : ActivityRcvMainContract.Presenter
{
    override var view: ActivityRcvMainContract.View

    private var networkController: NetworkController

    private var modelRcvMain:ModelRcvMain

    constructor(view: ActivityRcvMainContract.View)
    {
        this.view = view
        modelRcvMain = ModelRcvMain(view.getContext())
        networkController = NetworkController(view.getContext().applicationContext)
    }

    override fun sendHttpSuccess(gsonConvertData: HttpRcvItemData, msg: String,adapterRcvMain: AdapterRcvMain)
    {
        view.toastShow(msg)
        rcvAdapterUpdate(gsonConvertData, adapterRcvMain)

        modelRcvMain.saveDataSendHttpSuccess(gsonConvertData)

        view.dismissProgressDialog()
    }

    override fun sendHttpFail(msg: String,adapterRcvMain: AdapterRcvMain)
    {
        //인터넷은 연결, 서버에 문제가 있을시.
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

    override fun sendHttp(httpJudgeListener: HttpJudgeListener)
    {
        if ( true == networkController.isNetworkCheck())
        {
            view.showProgressDialog("통신 중 입니다.\n Data Size " + getDataSize())
            HttpRcvMain(httpJudgeListener, networkController, modelRcvMain.getDataSize()).sendHttp()
        }
        else
            view.toastShow("Please check, Network state")
    }

    override fun sendHttpRetry(httpJudgeListener: HttpJudgeListener, dataSize:Int)
    {
        modelRcvMain.saveHttpDataSize(dataSize)
        sendHttp(httpJudgeListener)
    }

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

    override fun getDataSize():Int = modelRcvMain.getDataSize()

    override fun onDestroy()
    {
        modelRcvMain.onDestroy()
    }
}
