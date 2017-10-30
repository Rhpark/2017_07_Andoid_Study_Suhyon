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
import com.rx.example.kotlintest001.view.dialog.CustomEditDialog
import com.rx.example.kotlintest001.view.presenter.ActivityRcvMainPresenter


class ActivityRcvMain : AppCompatActivity() {

    private var rcvMain: RecyclerView? = null

    private var btnRetry: Button? = null
    private var btnSearch: Button? = null

    private var pd: ProgressDialog? = null

    var ceDlgRetry: CustomEditDialog? = null
    var ceDlgSearch: CustomEditDialog? = null

    private var presenter : ActivityRcvMainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_rcv)

        findView()

        initData()

        initListener()

        presenter!!.sendHttp()
    }

    private fun findView()
    {
        rcvMain   = findViewById(R.id.rcvMain)  as RecyclerView
        btnRetry  = findViewById(R.id.btnRetry) as Button
        btnSearch = findViewById(R.id.btnSearch) as Button
    }

    private fun initData()
    {
        presenter = ActivityRcvMainPresenter(this,rcvMain!!)

        pd = ProgressDialog(this)
        presenter!!.pd = pd
    }

    private fun initListener()
    {
        rcvMain!!.addOnScrollListener( object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if ( isCheckRcvScrollBottom()) {
                    presenter!!.rcvShowAddValue(rcvMain!!)
                }
            }
        })

        btnRetry!!.setOnClickListener { clickBtnRetry() }
        btnSearch!!.setOnClickListener { clickBtnSearch() }
    }

    private fun clickBtnRetry()
    {
        val btnOkListener= DialogInterface.OnClickListener {
            dialogInterface, i ->

            presenter!!.clickBtnOkRetry(ceDlgRetry!!)
        }
        val btnCancelListener= DialogInterface.OnClickListener {
            dialogInterface, i ->   ceDlgRetry!!.closeKeyboard()
        }

        ceDlgRetry = CustomEditDialog(this, presenter!!.dataSize,
                InputType.TYPE_CLASS_NUMBER,btnOkListener, btnCancelListener)
        ceDlgRetry!!.showDlg()
    }

    private fun clickBtnSearch()
    {
        val btnOkListener= DialogInterface.OnClickListener {
            dialogInterface, i ->

            presenter!!.clickBtnOkSearch(ceDlgSearch!!)
        }
        val btnCancelListener= DialogInterface.OnClickListener {
            dialogInterface, i ->   ceDlgSearch!!.closeKeyboard()
        }

        ceDlgSearch = CustomEditDialog(this, presenter!!.dataSize,
                InputType.TYPE_CLASS_NUMBER,btnOkListener, btnCancelListener)
        ceDlgSearch!!.showDlg()
    }


    private fun isCheckRcvScrollBottom(): Boolean
    {
        val lastVisibleItemPosition = (rcvMain!!.getLayoutManager() as LinearLayoutManager)
                .findLastCompletelyVisibleItemPosition()
        val itemTotalCount = rcvMain!!.getAdapter().itemCount - 1

        if ( lastVisibleItemPosition == itemTotalCount )
            return true

        return false
    }
}
