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
//import com.rx.example.kotlintest001.model.realm.dto.RealmDTOHttpRcvItemData
import com.rx.example.kotlintest001.view.dialog.CustomEditDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.concurrent.TimeUnit

/**
 */

class ActivityRcvMainPresenter : ActivityRcvMainPresenterImp {

    private val activity: AppCompatActivity

    private var adapterRcvMain: AdapterRcvMain?         = null
    private var networkController: NetworkController?   = null
    private var httpRcvItemData: HttpRcvItemData?       = null
    private var httpJudgeListener: HttpJudgeListener?   = null

    var pd: ProgressDialog? = null
    var dataSize:Int = 5000

//    var realmDTOHttpRcvItemData: RealmDTOHttpRcvItemData?= null

    constructor(activity: AppCompatActivity, rcvMain: RecyclerView)
    {
        Logger.d()
        this.activity = activity

        Logger.d("dataSize $dataSize")
        initData(rcvMain)
    }


    private fun initData(rcvMain: RecyclerView)
    {
        Realm.init(activity)
//        realmDTOHttpRcvItemData = RealmDTOHttpRcvItemData(Realm.getDefaultInstance())
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

//                if ( realmDTOHttpRcvItemData != null)
//                    realmDTOHttpRcvItemData!!.delete()
//
//                realmDTOHttpRcvItemData!!.delete()
//                realmDTOHttpRcvItemData!!.save(httpRcvItemData!!.results)
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

    fun clickBtnOkRetry(ceDlg: CustomEditDialog)
    {
        ceDlg!!.closeKeyboard()

        if ( false == ceDlg!!.isGetNumber()) {
            toast("Can not Change Data Size\n please Input Data 1~5000")
            return
        }

        val size = ceDlg!!.getNumber()

        if ( size < 1 && size > 5000) return

        dataSize = size
        sendHttp()

    }

    fun clickBtnOkSearch(ceDlg: CustomEditDialog)
    {
//        ceDlg!!.closeKeyboard()
//
//        val key = ceDlg!!.getNumber()
//
//        if ( false == ceDlg!!.isGetNumber() || false == realmDTOHttpRcvItemData!!.isFindValue(key))
//        {
//            toast("Can not Search Data \n please Input Data number 0 ~ " + (realmDTOHttpRcvItemData!!.getDataSize() -1))
//            return
//        }

//        val result = realmDTOHttpRcvItemData!!.getFindValue(key)

//        Logger.d("result No $key , "+ result.name.getFullName() + " gender " + result.gender)
    }

    private fun showProgressDialog(msg:String)
    {
        if ( pd!!.isShowing )
            pd!!.dismiss()
        pd!!.setMessage(msg)
        pd!!.show()
    }
}
