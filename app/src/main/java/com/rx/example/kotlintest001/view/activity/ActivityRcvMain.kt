package com.rx.example.kotlintest001.view.activity

import android.app.ProgressDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.widget.Button
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.network.http.HttpJudgeListener
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg
import com.rx.example.kotlintest001.view.dialog.CustomDlgResultInfo
import com.rx.example.kotlintest001.view.presenter.ActivityRcvMainPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


class ActivityRcvMain : AppCompatActivity() {


    private val rcvMain         by lazy{    findViewById(R.id.rcvMain)  as RecyclerView }
    private val btnRetry        by lazy{    findViewById(R.id.btnRetry) as Button }
    private val btnSearch       by lazy{    findViewById(R.id.btnSearch) as Button }
    private val pd              by lazy{    ProgressDialog(this) }
    private val adapterRcvMain  by lazy{    AdapterRcvMain(this) }

    private var presenter : ActivityRcvMainPresenter by Delegates.notNull()
    private var httpJudgeListener: HttpJudgeListener by Delegates.notNull()

    private var disposable: Disposable by Delegates.notNull()   //recyclerview ItemSelect

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_rcv)

        presenter = ActivityRcvMainPresenter(this, pd)

        initListener()

        presenter.sendHttp(httpJudgeListener)
    }

    private fun initListener()
    {
        btnRetry.setOnClickListener { clickBtnRetry() }

        btnSearch.setOnClickListener { clickBtnSearch() }

        //RecyclerView Item Click Event
        disposable = adapterRcvMain.clickEvent
                .subscribe({
                    CustomDlgResultInfo(this, adapterRcvMain.selectPosition, it).show()
                })

        rcvMain.addOnScrollListener( object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if ( isCheckRcvScrollBottom() && presenter.isRcvAddValue(adapterRcvMain))
                    rcvMovoToPositon(presenter.rcvShowAddValue(rcvMain, adapterRcvMain))
            }
        })

        httpJudgeListener = object : HttpJudgeListener
        {
            override fun success(gsonConvertData: Any, msg: String)
            {
                presenter.sendHttpSuccess(gsonConvertData,msg,rcvMain,adapterRcvMain)
            }

            override fun fail(msg: String)
            {
                presenter.sendHttpFail(msg, rcvMain, adapterRcvMain)
            }
        }
    }

    private fun rcvMovoToPositon(currentPosition:Int) {
        //        rcvMain.smoothScrollToPosition(adapterRcvMain.listSize - AdapterRcvMain.MAX_ADD_VALUE )
        //same Thread Sleep Type
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//결과 스레드 설정
                .subscribe {
                    presenter.rcvMoveToPosition(rcvMain, (adapterRcvMain.listSize - currentPosition))

                }
    }

    private fun clickBtnRetry()
    {
        var ceDlgRetry : AlertEditDlg? = null
        val btnOkListener= DialogInterface.OnClickListener {
            dialogInterface, i ->
            if ( presenter.isCheckRetry(ceDlgRetry!!) )
            {
                presenter.clickBtnOkRetry(ceDlgRetry!!)
                presenter.sendHttp(httpJudgeListener)
            }
        }

        val btnCancelListener= DialogInterface.OnClickListener {

            dialogInterface, i ->   ceDlgRetry!!.closeKeyboard()
        }

        ceDlgRetry = AlertEditDlg(this
                , presenter.getDataSize()
                , InputType.TYPE_CLASS_NUMBER
                , "Retry send http data size"
                , "Change the data size 1~5000"
                , btnOkListener, btnCancelListener)
        ceDlgRetry!!.showDlg()
    }

    private fun clickBtnSearch()
    {
        var ceDlgSearch : AlertEditDlg? = null

        val btnOkListener= DialogInterface.OnClickListener {

            dialogInterface, i ->
            presenter.clickSearchDlgBtnOk(ceDlgSearch!!)
        }

        val btnCancelListener= DialogInterface.OnClickListener {

            dialogInterface, i ->   ceDlgSearch!!.closeKeyboard()
        }

        ceDlgSearch = AlertEditDlg(this
                , (presenter.getDataSize()-1)
                , InputType.TYPE_CLASS_NUMBER
                , "Search Data Type Number"
                , "Search the data No. 1 ~ "+ (presenter.getDataSize()-1)
                , btnOkListener, btnCancelListener)
        ceDlgSearch!!.showDlg()
    }

    private fun isCheckRcvScrollBottom(): Boolean
    {
        val lastVisibleItemPosition = (rcvMain.getLayoutManager() as LinearLayoutManager)
                .findLastCompletelyVisibleItemPosition()
        val itemTotalCount = rcvMain.getAdapter().itemCount - 1

        if ( lastVisibleItemPosition == itemTotalCount )
            return true

        return false
    }

    override fun onDestroy()
    {
        super.onDestroy()
        disposable.dispose()
        presenter.onDestroy()
    }
}
