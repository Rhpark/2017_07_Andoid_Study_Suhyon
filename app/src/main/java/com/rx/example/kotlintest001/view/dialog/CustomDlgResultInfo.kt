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
import kotlin.properties.Delegates

class CustomDlgResultInfo : Dialog {

    val selectNumber:Int
    val result: Result

    var tvCount:        TextView by Delegates.notNull()
    var tvName:         TextView by Delegates.notNull()
    var tvGender:       TextView by Delegates.notNull()
    var tvDob:          TextView by Delegates.notNull()
    var tvRegistered:   TextView by Delegates.notNull()
    var tvCity:         TextView by Delegates.notNull()
    var tvEmail:        TextView by Delegates.notNull()
    var tvPhone:        TextView by Delegates.notNull()

    var ivPicture:  ImageView by Delegates.notNull()

    var btnOk:  Button by Delegates.notNull()

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

        findview()
        initData()

        btnOk!!.setOnClickListener { this.dismiss() }
    }

    private fun findview()
    {
        tvCount     = findViewById(R.id.tvCount) as TextView
        tvName      = findViewById(R.id.tvName) as TextView
        tvGender    = findViewById(R.id.tvGender) as TextView
        tvDob       = findViewById(R.id.tvDob) as TextView
        tvCity      = findViewById(R.id.tvCity) as TextView
        tvEmail     = findViewById(R.id.tvEmail) as TextView
        tvPhone     = findViewById(R.id.tvPhone) as TextView
        tvRegistered = findViewById(R.id.tvRegistered) as TextView

        ivPicture = findViewById(R.id.ivPicture) as ImageView

        btnOk = findViewById(R.id.btnOk) as Button
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

        Glide.with(this.context).load(result.picture!!.large).into(ivPicture)
    }
}