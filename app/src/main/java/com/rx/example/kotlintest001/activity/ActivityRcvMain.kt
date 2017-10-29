package com.rx.example.kotlintest001.activity

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.activity.presenter.ActivityRcvMainPresenter
import com.rx.example.kotlintest001.network.http.HttpRcvMain


class ActivityRcvMain : AppCompatActivity() {

    private var rcvMain: RecyclerView? = null
    private var btnRetry: Button? = null
    private var pd: ProgressDialog? = null

    private var presenter : ActivityRcvMainPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_rcv)

        findView()

        presenter = ActivityRcvMainPresenter(this,rcvMain!!)

        pd = ProgressDialog(this)
        showProgressDialog("통신 중 입니다.\n Data Size " + HttpRcvMain.DATA_SIZE)

        presenter!!.pd = pd

        presenter!!.sendHttp()

        rcvMain!!.addOnScrollListener( object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if ( rcvBottomScrollCheck()) {
                    showProgressDialog("추가 정보를 가져옵니다.")
                    presenter!!.rcvShowAddValue(rcvMain!!)
                }
            }
        })

        btnRetry!!.setOnClickListener {
            showProgressDialog("Retry..")
            presenter!!.sendHttp()
        }
    }

    private fun showProgressDialog(msg:String)
    {
        if ( pd!!.isShowing )
            pd!!.dismiss()
        pd!!.setMessage(msg)
        pd!!.show()
    }

    private fun findView()
    {
        rcvMain = findViewById(R.id.rcvMain) as RecyclerView
        btnRetry= findViewById(R.id.btnRetry)as Button
    }

    private fun rcvBottomScrollCheck(): Boolean
    {
        val lastVisibleItemPosition = (rcvMain!!.getLayoutManager() as LinearLayoutManager)
                .findLastCompletelyVisibleItemPosition()
        val itemTotalCount = rcvMain!!.getAdapter().itemCount - 1

        if ( lastVisibleItemPosition == itemTotalCount )
            return true

        return false
    }
}
