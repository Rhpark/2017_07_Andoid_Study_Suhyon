package com.rx.example.kotlintest001.view.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.widget.Toast
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.adapter.AdapterRcvMain
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.view.dialog.AlertEditDlg
import com.rx.example.kotlintest001.view.dialog.CustomDlgResultInfo
import com.rx.example.kotlintest001.view.presenter.ActivityRcvMainContract
import com.rx.example.kotlintest001.view.presenter.ActivityRcvMainPresenter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main_rcv.*


class ActivityRcvMain : AppCompatActivity(), ActivityRcvMainContract.View
{
    private lateinit var pd : ProgressDialog

    private lateinit var adapterRcvMain: AdapterRcvMain

    private lateinit var presenter: ActivityRcvMainPresenter

    private lateinit var dspbRecyclerViewItemclick:  Disposable

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

        if ( null == savedInstanceState )   presenter.onStartSendHttp(adapterRcvMain)
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

        presenter.listener(adapterRcvMain)
    }

    private fun getCurrentRcvPosition() =
            (rcvMain.getLayoutManager() as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

    private fun clickBtnRetry()
    {
        if ( false == presenter.isCheckNetworkState() ) return

        var ceDlgRetry  = AlertEditDlg(this, adapterRcvMain.getResultSize(), InputType.TYPE_CLASS_NUMBER
                , "Retry send http data size", "Change the data size 1 ~ " + presenter.DefaultMaxDataSize)

        ceDlgRetry.showDlg()
        ceDlgRetry.psBtnClick
                .subscribe {

                    ceDlgRetry.closeKeyboard()

                    val isGetNumber = ceDlgRetry.isGetNumber()

                    if ( it == true  && isGetNumber)
                        presenter.sendHttpRetry(ceDlgRetry.getNumber())

                    else if(false == isGetNumber)
                        toastShow("Can not Change Data Size\n please Input Integer number")
                }
    }

    private fun clickBtnSearch()
    {
        val maxsize = ( adapterRcvMain.getResultSize() - 1 )
        if ( maxsize < 0 )
        {
            toastShow("Empty Data,Please Retry")
            return
        }
        var ceDlgSearch = AlertEditDlg(this, maxsize, InputType.TYPE_CLASS_NUMBER
                , "Search Data Type Number", "Search the data No. 0 ~ "+ maxsize)

        ceDlgSearch.showDlg()

        ceDlgSearch.psBtnClick
                .subscribe {

                    ceDlgSearch.closeKeyboard()

                    val isGetNumber = ceDlgSearch.isGetNumber()

                    if ( it == true && isGetNumber)
                    {
                        val value = ceDlgSearch.getNumber()

                        if ( value >=  adapterRcvMain.getResultSize())
                            toastShow("Con not open Dialog, Please Check Search Number")
                        else
                            CustomDlgResultInfo(this, value, adapterRcvMain.results.get(value)).show()
                    }
                    else if ( false == isGetNumber )
                        toastShow("Can not open dialoge\n please Input Integer number")
                }
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
