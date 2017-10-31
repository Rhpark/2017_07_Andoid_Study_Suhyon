package com.rx.example.kotlintest001.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.model.http.dao.Result
import com.rx.example.kotlintest001.model.realm.dto.RealmHttpRcvDTO
import kotlin.properties.Delegates

class CustomDlgResultInfo : Dialog {

    val selectNumber:Int
    val result: Result

    private val tvCount        by lazy{    findViewById(R.id.tvCount) as TextView }
    private val tvName         by lazy{    findViewById(R.id.tvName) as TextView }
    private val tvGender       by lazy{    findViewById(R.id.tvGender) as TextView }
    private val tvDob          by lazy{    findViewById(R.id.tvDob) as TextView }
    private val tvRegistered   by lazy{    findViewById(R.id.tvCity) as TextView }
    private val tvCity         by lazy{    findViewById(R.id.tvEmail) as TextView }
    private val tvEmail        by lazy{    findViewById(R.id.tvPhone) as TextView }
    private val tvPhone        by lazy{    findViewById(R.id.tvRegistered) as TextView }
    private val ivPicture      by lazy{    findViewById(R.id.ivPicture) as ImageView }
    private val btnOk          by lazy{    findViewById(R.id.btnOk) as Button }

    constructor(context: Context?, selectNumber: Int, result: Result) : super(context)
    {
        this.selectNumber = selectNumber
        this.result = result
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(R.layout.custom_dlg_result_info)

        initData()

        btnOk.setOnClickListener { this.dismiss() }
    }

    private fun initData()
    {
        tvCount.setText("No. "+selectNumber)
        tvGender.setText(result.gender)
        tvName.setText(result.name!!.fullName())
        tvCity.setText(result.location!!.city)
        tvEmail.setText(result.email)
        tvPhone.setText(result.phone)
        tvRegistered.setText(result.registered)
        tvDob.setText(result.dob)

        Glide.with(this.context).load(result.picture?.large).into(ivPicture)
    }
}
