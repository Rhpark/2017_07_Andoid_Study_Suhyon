package com.rx.example.kotlintest001.model

import android.content.Context
import com.rx.example.kotlintest001.model.http.dao.Result
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.model.realm.dto.RealmHttpRcvDTO
import com.rx.example.kotlintest001.model.sharedpf.SharedPfRcvMainDataSize
import com.rx.example.kotlintest001.view.presenter.ActivityRcvMainContract

/**
 * Created by INNO_14 on 2017-11-01.
 */
class ModelRcvMain : ActivityRcvMainContract.Model{

    private val context: Context

    private val realmHttpRcvDTO by lazy{    RealmHttpRcvDTO() }

    constructor(context: Context)
    {
        this.context = context
    }

    override fun saveDataSendHttpSuccess(httpRcvItemData: HttpRcvItemData)
    {
        deleteAllData()
        saveHttpData(httpRcvItemData)
//        realmHttpRcvDTO.insertAll(httpRcvItemData, context)
    }

    override fun saveDataSendHttpFail()
    {
        loadAllData()
    }

    override fun getDataSize(): Int = realmHttpRcvDTO.httpRcvItemData!!.results!!.size

    override fun isGetResultData(): Boolean = realmHttpRcvDTO.isGetData()

    override fun getHttpData(): HttpRcvItemData = realmHttpRcvDTO.httpRcvItemData!!

    override fun getHttpResults(): MutableList<Result> = realmHttpRcvDTO.httpRcvItemData!!.results!!

    private fun saveHttpData(httpRcvItemData: HttpRcvItemData) = realmHttpRcvDTO.insertAll(httpRcvItemData, context)

    fun loadAllData()
    {
        realmHttpRcvDTO.loadData()
    }

    private fun deleteAllData()
    {
        realmHttpRcvDTO.delete()
    }

    override fun isRcvAddSizeUp(lastSize: Int): Boolean {

        if ( realmHttpRcvDTO.httpRcvItemData == null)
            return false

        val resultSize = getHttpData().results!!.size

        val TotalSize = resultSize
        val defSize   = TotalSize - lastSize

        if ( TotalSize <= lastSize || resultSize <= defSize)
            return false

        return true
    }

    override fun onDestroy()
    {
        realmHttpRcvDTO.onDestroy()
    }
}