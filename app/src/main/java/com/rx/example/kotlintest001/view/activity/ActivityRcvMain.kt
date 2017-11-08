package com.rx.example.kotlintest001.view.activity

import android.app.ProgressDialog
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


class ActivityRcvMain : AppCompatActivity(), ActivityRcvMainContract.View
{
    private val rcvMain     by lazy{    findViewById(R.id.rcvMain)  as RecyclerView }
    private val btnRetry    by lazy{    findViewById(R.id.btnRetry) as Button }
    private val btnSearch   by lazy{    findViewById(R.id.btnSearch) as Button }

    private lateinit var pd : ProgressDialog

    private lateinit var adapterRcvMain: AdapterRcvMain

    private lateinit var presenter: ActivityRcvMainPresenter

    private var dspbRecyclerViewItemclick:  Disposable by Delegates.notNull()   //recyclerview ItemSelect

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Logger.d()
        setContentView(R.layout.activity_main_rcv)

        adapterRcvMain = AdapterRcvMain()
        pd = ProgressDialog(this).apply { setCanceledOnTouchOutside(false) }
        presenter = ActivityRcvMainPresenter(this@ActivityRcvMain, applicationContext)

        initListener()

        rcvMain.adapter = adapterRcvMain

        if ( null == savedInstanceState )   presenter.onStartSendHttp()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?)
    {
        super.onRestoreInstanceState(savedInstanceState)
        Logger.d()

        if ( savedInstanceState == null) return

        val currentRcvPosition = savedInstanceState.getInt("currentRcvPosition")
        val adapterListItemCount = savedInstanceState.getInt("adapterListItemCount")
        Logger.d(" if currentRcvPosition $currentRcvPosition  adapterListItemCount $adapterListItemCount")

        if ( presenter.savedInstanceState(adapterRcvMain, adapterListItemCount))
            rcvMain.scrollToPosition(currentRcvPosition)
    }


    private fun initListener()
    {
        //buttonClick
        btnRetry.setOnClickListener { clickBtnRetry() }

        btnSearch.setOnClickListener { clickBtnSearch() }

        //recyclerViewItemCheck
        dspbRecyclerViewItemclick = adapterRcvMain.psRcvItemSelected
                .subscribe {
                    Logger.d()
                    CustomDlgResultInfo(this, it, adapterRcvMain.getItem(it)).show()
                }

        //recyclerViewBottomCheck -> ItemSizeUp
        rcvMain.addOnScrollListener( object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val currentposition = getCurrentRcvPosition()

                if ( true == presenter.isCheckAdapterItemSizeAdd(currentposition, adapterRcvMain.itemCount, adapterRcvMain.results.size) )
                    rcvMovoToPositon(presenter.rcvShowAddValue(adapterRcvMain))
            }
        })
        presenter.listener(adapterRcvMain)
    }

    private fun getCurrentRcvPosition() =
            (rcvMain.getLayoutManager() as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

    private fun rcvMovoToPositon(currentPosition:Int)
    {

        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//결과 스레드 설정
                .subscribe {
                    rcvMain.smoothScrollToPosition(currentPosition )
                    dismissProgressDialog()
                }
    }

    private fun clickBtnRetry()
    {
        var ceDlgRetry : AlertEditDlg? = null
        val btnOkListener= DialogInterface.OnClickListener {
            dialogInterface, i ->
            if ( presenter.isCheckRetry(ceDlgRetry!!) )
                presenter.sendHttpRetry(ceDlgRetry!!.getNumber())
        }

        val btnCancelListener= DialogInterface.OnClickListener {
            dialogInterface, i ->
            ceDlgRetry?.closeKeyboard()
        }

        ceDlgRetry = AlertEditDlg(this
                , adapterRcvMain.results.size
                , InputType.TYPE_CLASS_NUMBER
                , "Retry send http data size"
                , "Change the data size 1~5000"
                , btnOkListener, btnCancelListener)
        ceDlgRetry?.showDlg()
    }

    private fun clickBtnSearch()
    {
        var ceDlgSearch : AlertEditDlg? = null

        val btnOkListener= DialogInterface.OnClickListener {
            dialogInterface, i ->
            if ( true == presenter.isCheckSearchDlgBtnOk( ceDlgSearch!!, adapterRcvMain.results.size ))
            {
                val value = ceDlgSearch!!.getNumber()
                CustomDlgResultInfo(this, value, adapterRcvMain.results.get( value )).show()
            }
        }

        val btnCancelListener= DialogInterface.OnClickListener {
            dialogInterface, i ->
            ceDlgSearch!!.closeKeyboard()
        }

        val maxsize = ( adapterRcvMain.results.size - 1 )
        ceDlgSearch = AlertEditDlg(this
                , maxsize
                , InputType.TYPE_CLASS_NUMBER
                , "Search Data Type Number"
                , "Search the data No. 0 ~ "+ maxsize
                , btnOkListener, btnCancelListener)
        ceDlgSearch?.showDlg()
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

    override fun onSaveInstanceState(outState: Bundle?)
    {
        super.onSaveInstanceState(outState)
        Logger.d()
        val currentRcvPosition = getCurrentRcvPosition()-1
        val adapterListItemCount = rcvMain.adapter.itemCount
        outState?.putInt("currentRcvPosition",currentRcvPosition)
        outState?.putInt("adapterListItemCount",adapterListItemCount)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        Logger.d()
        dspbRecyclerViewItemclick.dispose()
        presenter.onDestroy()
    }
}
