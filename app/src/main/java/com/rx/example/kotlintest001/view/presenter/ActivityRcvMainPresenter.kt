package com.rx.example.kotlintest001.view.presenter

import android.app.ProgressDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.network.http.HttpJudgeListener
import com.rx.example.kotlintest001.network.http.HttpRcvMain
import com.rx.example.kotlintest001.model.http.HttpRcvItemData
import com.rx.example.kotlintest001.view.dialog.CustomEditDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 */

class ActivityRcvMainPresenter : ActivityRcvMainPresenterImp {

    private val activity: AppCompatActivity

    private var adapterRcvMain: AdapterRcvMain?         = null
    private var networkController: NetworkController?   = null
    private var httpRcvItemData: HttpRcvItemData?        = null
    private var httpJudgeListener: HttpJudgeListener?   = null

    var pd: ProgressDialog? = null


    var dataSize = 5000

    constructor(activity: AppCompatActivity, rcvMain: RecyclerView)
    {
        Logger.d()
        this.activity = activity

        initData(rcvMain)
    }

    private fun initData(rcvMain: RecyclerView)
    {
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
    }

    fun toast(msg:String) = Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show()

    private fun closeProgressDialog()
    {
        if ( pd!!.isShowing)    pd!!.dismiss()
    }

    override fun sendHttp() {
        showProgressDialog("통신 중 입니다.\n Data Size " + dataSize)
        HttpRcvMain(httpJudgeListener, networkController!!, dataSize)!!.sendHttp()
    }

    override fun rcvShowAddValue(rcvMain: RecyclerView)
    {
        if ( httpRcvItemData == null)
            return

        val TotalSize = httpRcvItemData!!.results.size
        val lastSize  = adapterRcvMain!!.listSize
        val defSize   = TotalSize - lastSize

        if ( TotalSize <= lastSize || httpRcvItemData!!.results.size <= defSize)
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

    fun clickBtnRetry()
    {
        var ced : CustomEditDialog? = null

        val btnOkListener= DialogInterface.OnClickListener {
            dialogInterface, i ->

            ced!!.closeKeyboard()

            if ( ced!!.isGetNumber())
            {
                dataSize = ced!!.getNumber()
                sendHttp()
            }
            else
                toast("Can not Change Data Size\n please Input Data 1~5000")
        }
        val btnCancelListener= DialogInterface.OnClickListener {
            dialogInterface, i ->   ced!!.closeKeyboard()
        }

        ced = CustomEditDialog(activity, dataSize, btnOkListener, btnCancelListener)
        ced.showDlg()
    }

    private fun showProgressDialog(msg:String)
    {
        if ( pd!!.isShowing )
            pd!!.dismiss()
        pd!!.setMessage(msg)
        pd!!.show()
    }
}
