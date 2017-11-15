package com.rx.example.kotlintest001.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.bumptech.glide.Glide
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.model.http.dto.Result
import kotlinx.android.synthetic.main.custom_dlg_result_info.*

class CustomDlgResultInfo : Dialog
{
    private val selectNumber:Int
    private val result: Result

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
        tvCity.setText(result.location.city)
        tvRegistered.setText(result.registered)
        tvName.setText(result.name.fullName())

        result.picture?.large?.let { Glide.with(this.context).load(it).into(ivPicture) }
    }
}
