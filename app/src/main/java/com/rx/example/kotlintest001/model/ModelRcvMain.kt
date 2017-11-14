package com.rx.example.kotlintest001.model

import android.content.Context
import com.rx.example.kotlintest001.model.http.dao.HttpRcvItemData
import com.rx.example.kotlintest001.model.realm.dto.RealmHttpRcvDTO
import com.rx.example.kotlintest001.view.presenter.ActivityRcvMainContract

/**
 * Created by INNO_14 on 2017-11-01.
 */
class ModelRcvMain : ActivityRcvMainContract.Model
{
    private val context: Context

    val realmHttpRcvDTO by lazy{ RealmHttpRcvDTO() }

    constructor(context: Context)
    {
        this.context = context
    }

    override fun saveDataSendHttpSuccess(httpRcvItemData: HttpRcvItemData)
    {
        deleteAllData()
        saveHttpData(httpRcvItemData)
    }

    private fun deleteAllData() {   realmHttpRcvDTO.delete()    }

    private fun saveHttpData(httpRcvItemData: HttpRcvItemData) = realmHttpRcvDTO.insertAll(httpRcvItemData, context)

    override fun isGetResultData(): Boolean = realmHttpRcvDTO.isGetData()

    override fun getHttpData(): HttpRcvItemData? = realmHttpRcvDTO.httpRcvItemData

    override fun loadAllData() {    realmHttpRcvDTO.loadData()  }

    override fun onDestroy()
    {
        realmHttpRcvDTO.onDestroy()
    }
}