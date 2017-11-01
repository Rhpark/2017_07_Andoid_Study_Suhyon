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
    private var sharedPfRcvMainDataSize: SharedPfRcvMainDataSize

    constructor(context: Context)
    {
        this.context = context
        sharedPfRcvMainDataSize = SharedPfRcvMainDataSize(context)
        sharedPfRcvMainDataSize.openDataSize()
    }

    override fun saveDataSendHttpSuccess(httpRcvItemData: HttpRcvItemData)
    {
        saveHttpData(httpRcvItemData)
        deleteAllData()
        realmHttpRcvDTO.insertAll(httpRcvItemData, context)
        saveHttpDataSize(httpRcvItemData.results!!.size)
    }

    override fun saveDataSendHttpFail()
    {
        loadAllData()
        saveHttpDataSize(realmHttpRcvDTO.httpRcvItemData!!.results!!.size)
    }

    override fun saveHttpDataSize(dataSize: Int) = sharedPfRcvMainDataSize.saveDataSize( dataSize)

    override fun getDataSize(): Int = sharedPfRcvMainDataSize.openDataSize()

    override fun isGetResultData(): Boolean = realmHttpRcvDTO.isGetData()

    override fun getHttpData(): HttpRcvItemData = realmHttpRcvDTO.getData()

    override fun getHttpResults(): MutableList<Result> = realmHttpRcvDTO.getData().results!!

    private fun saveHttpData(httpRcvItemData: HttpRcvItemData) = realmHttpRcvDTO.insertAll(httpRcvItemData, context)

    private fun loadAllData()
    {
        realmHttpRcvDTO.loadData()
        sharedPfRcvMainDataSize.openDataSize()
    }

    private fun deleteAllData()
    {
        realmHttpRcvDTO.delete()
        sharedPfRcvMainDataSize.deleteData()
    }

    override fun isRcvAddSizeUp(lastSize: Int): Boolean {

        if ( realmHttpRcvDTO.httpRcvItemData == null)
            return false

        val results = getHttpData().results

        val TotalSize = results!!.size
        val defSize   = TotalSize - lastSize

        if ( TotalSize <= lastSize || results!!.size <= defSize)
            return false

        return true
    }

    override fun onDestroy()
    {
        realmHttpRcvDTO.onDestroy()
    }
}