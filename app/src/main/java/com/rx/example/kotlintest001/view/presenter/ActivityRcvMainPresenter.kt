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
import com.rx.example.kotlintest001.model.http.HttpRcvItemData
import com.rx.example.kotlintest001.model.sharedpf.SharedPfRcvMainDataSize
import com.rx.example.kotlintest001.view.dialog.CustomDlgResultInfo
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 */

class ActivityRcvMainPresenter : ActivityRcvMainPresenterImp {

    private val activity: AppCompatActivity

    private var networkController: NetworkController?   = null
    private var httpJudgeListener: HttpJudgeListener?   = null
    private var sharedPfRcvMainDataSize : SharedPfRcvMainDataSize? = null

    var adapterRcvMain: AdapterRcvMain?         = null
    var httpRcvItemData: HttpRcvItemData?        = null

    var pd: ProgressDialog? = null

    private var disposable: Disposable? = null

    constructor(activity: AppCompatActivity, rcvMain: RecyclerView)
    {
        Logger.d()
        this.activity = activity

        initData(rcvMain)
    }

    private fun initData(rcvMain: RecyclerView)
    {
        sharedPfRcvMainDataSize = SharedPfRcvMainDataSize(activity.applicationContext)
        sharedPfRcvMainDataSize!!.openDataSize()

        networkController = NetworkController()

        adapterRcvMain = AdapterRcvMain(activity)

        httpJudgeListener = object : HttpJudgeListener {
            override fun success(gsonConvertData: Any, msg: String) {
                toast(msg)
                closeProgressDialog()
                httpRcvItemData = gsonConvertData as HttpRcvItemData
                adapterRcvMain!!.httpRcvItemData = httpRcvItemData
                adapterRcvMain!!.listSizeCheck()
                rcvMain!!.adapter = adapterRcvMain
                adapterRcvMain!!.notifyDataSetChanged()
            }

            override fun fail(msg: String) {
                toast(msg)
                closeProgressDialog()
            }
        }

        disposable = adapterRcvMain!!.clickEvent
                .subscribe({
                    CustomDlgResultInfo(activity, adapterRcvMain!!.selectPosition, it).show()
                })
    }

    fun toast(msg:String) = Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show()

    private fun closeProgressDialog()
    {
        if ( pd!!.isShowing)    pd!!.dismiss()
    }

    override fun sendHttp()
    {
        showProgressDialog("통신 중 입니다.\n Data Size " + sharedPfRcvMainDataSize!!.dataSize)
        HttpRcvMain(httpJudgeListener, networkController!!, sharedPfRcvMainDataSize!!.dataSize!!)!!.sendHttp()
    }

    override fun rcvShowAddValue(rcvMain: RecyclerView)
    {
        if ( httpRcvItemData == null)
            return

        val TotalSize = httpRcvItemData!!.results!!.size
        val lastSize  = adapterRcvMain!!.listSize
        val defSize   = TotalSize - lastSize

        if ( TotalSize <= lastSize || httpRcvItemData!!.results!!.size <= defSize)
            return

        showProgressDialog("추가 정보를 가져옵니다.")

        var maxSize = AdapterRcvMain.MAX_ADD_VALUE

        if (defSize <  maxSize)
            maxSize = defSize

        adapterRcvMain!!.listSize += maxSize

        adapterRcvMain!!.notifyDataSetChanged()

        rcvShowAddValueMovoToPositon(rcvMain!!, maxSize)
    }

    private fun rcvShowAddValueMovoToPositon(rcvMain: RecyclerView, currentPosition:Int) {
        //        rcvMain.smoothScrollToPosition(adapterRcvMain.listSize - AdapterRcvMain.MAX_ADD_VALUE )
        //same Thread Sleep Type
        Observable.timer(500,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//결과 스레드 설정
                .subscribe {
                    rcvMain.smoothScrollToPosition(adapterRcvMain!!.listSize - currentPosition )
                    closeProgressDialog()
                }
    }

    fun clickBtnOkRetry(ceDlgRetry : AlertEditDlg)
    {
        ceDlgRetry.closeKeyboard()

        if ( ceDlgRetry.isGetNumber())
        {
            val getValue = ceDlgRetry.getNumber()
            if ( getValue  < 1 || getValue > 5000)
                toast("Can not Change Data Size\n please Input Number Size 1~5000")

            else
            {
                sharedPfRcvMainDataSize!!.saveDataSize( ceDlgRetry.getNumber() )
                sendHttp()
            }
        }
        else
            toast("Can not Change Data Size\n please Input number")
    }

    fun clickBtnOkSearch(ceDlgRetry : AlertEditDlg):Boolean
    {
        ceDlgRetry.closeKeyboard()

        if ( ceDlgRetry.isGetNumber())
        {
            val getValue = ceDlgRetry.getNumber()
            if ( getValue  < 0 || getValue > (getDataSize()-1))
            {
                toast("Can not Change Data Size\n please Input Number Size 1 ~ " + (getDataSize()-1))
                return false
            }
            CustomDlgResultInfo(activity,getValue,httpRcvItemData!!.results!!.get(getValue)).show()

            toast("find Data!")
            return true
        }

        toast("Can not Change Data Size\n please Input number")
        return false
    }

    fun getDataSize():Int = sharedPfRcvMainDataSize!!.dataSize!!

    private fun showProgressDialog(msg:String)
    {
        if ( pd!!.isShowing )
            pd!!.dismiss()
        pd!!.setMessage(msg)
        pd!!.show()
    }

    fun onDestroy() = disposable?.dispose()
}
