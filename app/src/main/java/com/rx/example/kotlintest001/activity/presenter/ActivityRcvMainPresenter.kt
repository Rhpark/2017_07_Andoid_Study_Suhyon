package com.rx.example.kotlintest001.activity.presenter

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.network.NetworkController
import com.rx.example.kotlintest001.network.http.HttpJudgeListener
import com.rx.example.kotlintest001.network.http.HttpRcvMain
import com.rx.example.kotlintest001.network.model.HttpRcvItemData
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
    private var httpRcvItemData:HttpRcvItemData?        = null
    private var httpJudgeListener: HttpJudgeListener?   = null

    var pd: ProgressDialog? = null

    constructor(activity: AppCompatActivity, rcvMain: RecyclerView) {
        Logger.d()
        this.activity = activity

        initData(rcvMain)
    }

    private fun initData(rcvMain: RecyclerView) {
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

    override fun sendHttp() {

        HttpRcvMain(httpJudgeListener, networkController!!)!!.sendHttp()
    }

    private fun toast(msg:String) = Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show()

    private fun closeProgressDialog() {
        if ( pd!!.isShowing)
            pd!!.dismiss()
    }

    override fun rcvShowAddValue(rcvMain: RecyclerView) {

        if ( httpRcvItemData == null)
        {
            closeProgressDialog()
            return
        }

        val TotalSize = httpRcvItemData!!.results.size

        val lastSize = adapterRcvMain!!.listSize

        val defSize = TotalSize - lastSize

        if ( TotalSize <= lastSize || httpRcvItemData!!.results.size <= defSize)
        {
            closeProgressDialog()
            return
        }

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
}
