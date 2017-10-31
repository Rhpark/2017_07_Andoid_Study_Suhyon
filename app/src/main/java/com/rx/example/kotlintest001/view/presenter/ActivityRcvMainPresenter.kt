package com.rx.example.kotlintest001.view.presenter

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.network.http.HttpJudgeListener
import com.rx.example.kotlintest001.network.http.HttpRcvMain
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.model.realm.dto.RealmHttpRcvDTO
import com.rx.example.kotlintest001.model.sharedpf.SharedPfRcvMainDataSize
import com.rx.example.kotlintest001.view.dialog.CustomDlgResultInfo
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg
import kotlin.properties.Delegates


class ActivityRcvMainPresenter : ActivityRcvMainPresenterImp {

    private val activity: AppCompatActivity

    private var networkController:  NetworkController by Delegates.notNull()

    private var sharedPfRcvMainDataSize : SharedPfRcvMainDataSize by Delegates.notNull()

    private var realmHttpRcvDTO:    RealmHttpRcvDTO   by Delegates.notNull()

    private var httpRcvItemData: HttpRcvItemData by Delegates.notNull()

    private var pd: ProgressDialog by Delegates.notNull()

    constructor(activity: AppCompatActivity, pd: ProgressDialog)
    {
        Logger.d()
        this.activity = activity
        this.pd = pd
        initData()
    }

    private fun initData()
    {
        realmHttpRcvDTO = RealmHttpRcvDTO()
        sharedPfRcvMainDataSize = SharedPfRcvMainDataSize(activity.applicationContext)
        sharedPfRcvMainDataSize.openDataSize()
        networkController = NetworkController(activity.applicationContext)
    }

    override fun sendHttpSuccess(gsonConvertData: Any, msg: String,rcvMain: RecyclerView,adapterRcvMain: AdapterRcvMain)
    {
        toast(msg)
        httpRcvItemData = gsonConvertData as HttpRcvItemData

        rcvAdapterUpdate(httpRcvItemData, adapterRcvMain)
        rcvMain.adapter = adapterRcvMain

        realmHttpRcvDTO.delete()
        realmHttpRcvDTO.insertAll(httpRcvItemData, activity.applicationContext)

        closeProgressDialog()
    }

    override fun sendHttpFail(msg: String,rcvMain: RecyclerView,adapterRcvMain: AdapterRcvMain)
    {
        toast(msg)

        if ( realmHttpRcvDTO.isGetData())
        {
            httpRcvItemData = realmHttpRcvDTO.getData()
            rcvAdapterUpdate(httpRcvItemData, adapterRcvMain)
            rcvMain.adapter = adapterRcvMain
            toast("Load data from realm")
        }

        closeProgressDialog()
    }

    private fun rcvAdapterUpdate(httpRcvItemData: HttpRcvItemData, adapterRcvMain: AdapterRcvMain)
    {
        adapterRcvMain.httpRcvItemData = httpRcvItemData
        adapterRcvMain.listSizeCheck()
        adapterRcvMain.notifyDataSetChanged()
    }

    private fun toast(msg:String) = Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show()

    override fun sendHttp(httpJudgeListener: HttpJudgeListener)
    {
        if ( true == networkController.isNetworkCheck())
        {
            showProgressDialog("통신 중 입니다.\n Data Size " + getDataSize())
            HttpRcvMain(httpJudgeListener, networkController, sharedPfRcvMainDataSize.dataSize!!).sendHttp()
        }
        else
            toast("Please check, Network state")
    }

    override fun rcvMoveToPosition(rcvMain: RecyclerView, currentPosition:Int)
    {
        rcvMain.smoothScrollToPosition(currentPosition )
        closeProgressDialog()
    }

    override fun isRcvAddValue(adapterRcvMain: AdapterRcvMain):Boolean
    {
        if ( httpRcvItemData == null)
            return false

        val TotalSize = httpRcvItemData.results!!.size
        val lastSize  = adapterRcvMain.listSize
        val defSize   = TotalSize - lastSize

        if ( TotalSize <= lastSize || httpRcvItemData.results!!.size <= defSize)
            return false

        return true
    }

    override fun rcvShowAddValue(rcvMain: RecyclerView, adapterRcvMain: AdapterRcvMain) : Int
    {
        showProgressDialog("add Data..")

        val defSize   = httpRcvItemData.results!!.size - adapterRcvMain.listSize

        var moveToPosition = AdapterRcvMain.MAX_ADD_VALUE

        if (defSize <  moveToPosition)
            moveToPosition = defSize

        adapterRcvMain.listSize += moveToPosition

        adapterRcvMain.notifyDataSetChanged()

        return moveToPosition
    }

    override fun isCheckRetry(ceDlgRetry : AlertEditDlg):Boolean
    {
        ceDlgRetry.closeKeyboard()

        if ( ceDlgRetry.isGetNumber())
        {
            val getValue = ceDlgRetry.getNumber()
            if ( getValue  < 1 || getValue > 5000)
            {
                toast("Can not Change Data Size\n please Input Number Size 1~5000")
                return false
            }

            return true
        }
        else {
            toast("Can not Change Data Size\n please Input number")
            return false
        }
    }

    override fun clickBtnOkRetry(ceDlgRetry : AlertEditDlg)
            = sharedPfRcvMainDataSize.saveDataSize( ceDlgRetry.getNumber() )

    override fun clickSearchDlgBtnOk(ceDlgRetry : AlertEditDlg)
    {
        ceDlgRetry.closeKeyboard()

        if ( ceDlgRetry.isGetNumber())
        {
            val getValue = ceDlgRetry.getNumber()
            if ( getValue  < 0 || getValue > (getDataSize()-1))
            {
                toast("Can not Change Data Size\n please Input Number Size 1 ~ " + (getDataSize()-1))
                return
            }
            CustomDlgResultInfo(activity, getValue, httpRcvItemData.results!!.get(getValue) ).show()
            toast("find Data!")
            return
        }
        toast("Can not Change Data Size\n please Input number")
        return
    }

    override fun getDataSize():Int = sharedPfRcvMainDataSize.dataSize!!

    private fun closeProgressDialog()
    {
        if ( pd.isShowing)    pd.dismiss()
    }

    private fun showProgressDialog(msg:String)
    {
        if ( pd.isShowing )
            pd.dismiss()
        pd.setMessage(msg)
        pd.show()
    }

    override fun onDestroy()
    {
        realmHttpRcvDTO.onDestroy()
    }
}
