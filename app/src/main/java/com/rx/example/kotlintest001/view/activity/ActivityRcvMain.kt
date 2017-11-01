package com.rx.example.kotlintest001.view.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.widget.Button
import android.widget.Toast
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.network.http.HttpJudgeListener
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg
import com.rx.example.kotlintest001.view.dialog.CustomDlgResultInfo
import com.rx.example.kotlintest001.view.presenter.ActivityRcvMainContract
import com.rx.example.kotlintest001.view.presenter.ActivityRcvMainPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


class ActivityRcvMain : AppCompatActivity(), ActivityRcvMainContract.View {

    private val rcvMain     by lazy{    findViewById(R.id.rcvMain)  as RecyclerView }
    private val btnRetry    by lazy{    findViewById(R.id.btnRetry) as Button }
    private val btnSearch   by lazy{    findViewById(R.id.btnSearch) as Button }
    private val pd          by lazy{    ProgressDialog(this) }

    private lateinit var adapterRcvMain: AdapterRcvMain
    private lateinit var presenter: ActivityRcvMainPresenter

    private var httpJudgeListener: HttpJudgeListener by Delegates.notNull()

    private var disposable: Disposable by Delegates.notNull()   //recyclerview ItemSelect

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_rcv)

        adapterRcvMain = AdapterRcvMain()
        presenter = ActivityRcvMainPresenter(this@ActivityRcvMain)

        initListener()

        rcvMain.adapter = adapterRcvMain
        presenter.sendHttp(httpJudgeListener)
    }

    private fun initListener()
    {
        btnRetry.setOnClickListener { clickBtnRetry() }

        btnSearch.setOnClickListener { clickBtnSearch() }

        /*disposable =*/ adapterRcvMain.clickEvent
                .subscribe {
                    Logger.d()
                    CustomDlgResultInfo(this, it, adapterRcvMain.getItem(it)).show()
                }

        rcvMain.addOnScrollListener( object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val currentposition = (rcvMain.getLayoutManager() as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()

                if ( true == presenter.isCheckAdapterItemSizeAdd(currentposition, adapterRcvMain.itemCount) )
                    rcvMovoToPositon(presenter.rcvShowAddValue(adapterRcvMain))
            }
        })

        httpJudgeListener = object : HttpJudgeListener
        {
            override fun success(gsonConvertData: Any, msg: String)
                    = presenter.sendHttpSuccess(gsonConvertData as HttpRcvItemData ,msg,adapterRcvMain)

            override fun fail(msg: String)
                    = presenter.sendHttpFail(msg, adapterRcvMain)
        }
    }

    private fun rcvMovoToPositon(currentPosition:Int) {

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
                presenter.sendHttpRetry(httpJudgeListener,ceDlgRetry!!.getNumber())
        }

        val btnCancelListener= DialogInterface.OnClickListener {
            dialogInterface, i ->
            ceDlgRetry!!.closeKeyboard()
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
            if ( true == presenter.isCheckSearchDlgBtnOk( ceDlgSearch!! ))
            {
                val value = ceDlgSearch!!.getNumber()
                CustomDlgResultInfo(this, value, adapterRcvMain.results.get( value )).show()
            }
        }

        val btnCancelListener= DialogInterface.OnClickListener {
            dialogInterface, i ->
            ceDlgSearch!!.closeKeyboard()
        }

        ceDlgSearch = AlertEditDlg(this
                , (presenter.getDataSize()-1)
                , InputType.TYPE_CLASS_NUMBER
                , "Search Data Type Number"
                , "Search the data No. 1 ~ "+ (presenter.getDataSize()-1)
                , btnOkListener, btnCancelListener)
        ceDlgSearch!!.showDlg()
    }

    override fun dismissProgressDialog()
    {
        if ( pd.isShowing)    pd.dismiss()
    }

    override fun showProgressDialog(msg:String)
    {
        if ( pd.isShowing )
            pd.dismiss()
        pd.setMessage(msg)
        pd.show()
    }

    override fun toastShow(msg: String) = Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()

    override fun getContext(): Context = this.applicationContext

    override fun onDestroy()
    {
        super.onDestroy()
//        disposable1.dispose()
//        disposable2.dispose()
        presenter.onDestroy()
    }
}
