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
import com.rx.example.kotlintest001.model.http.dto.Result

class CustomDlgResultInfo : Dialog
{
    val selectNumber:Int
    val result: Result

    private val tvDob          by lazy{    findViewById(R.id.tvDob) as TextView }
    private val tvName         by lazy{    findViewById(R.id.tvName) as TextView }
    private val tvCity         by lazy{    findViewById(R.id.tvCity) as TextView }
    private val tvCount        by lazy{    findViewById(R.id.tvCount) as TextView }
    private val tvEmail        by lazy{    findViewById(R.id.tvEmail) as TextView }
    private val tvPhone        by lazy{    findViewById(R.id.tvPhone) as TextView }
    private val tvGender       by lazy{    findViewById(R.id.tvGender) as TextView }
    private val tvRegistered   by lazy{    findViewById(R.id.tvRegistered) as TextView }

    private val btnOk          by lazy{    findViewById(R.id.btnOk) as Button }

    private val ivPicture      by lazy{    findViewById(R.id.ivPicture) as ImageView }

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
        tvDob.setText(result.dob)
        tvPhone.setText(result.phone)
        tvEmail.setText(result.email)
        tvGender.setText(result.gender)
        tvCount.setText("No. "+selectNumber)
        tvCity.setText(result.location?.city)
        tvRegistered.setText(result.registered)
        tvName.setText(result.name?.fullName())

        Glide.with(this.context).load(result.picture?.large).into(ivPicture)
    }
}
