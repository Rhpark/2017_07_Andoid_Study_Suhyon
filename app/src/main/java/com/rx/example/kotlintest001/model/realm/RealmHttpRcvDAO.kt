package com.rx.example.kotlintest001.model.realm.dto

import android.content.Context
import android.widget.Toast
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.model.http.dao.Info
import com.rx.example.kotlintest001.model.http.dao.Result
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import io.reactivex.subjects.PublishSubject
import io.realm.Realm
import kotlin.properties.Delegates

/**
 * Created by Rhpark on 2017-10-30.
 */
class RealmHttpRcvDAO
{
    var httpRcvItemData: HttpRcvItemData? = null
    var psRealmlIsInserted: PublishSubject<Boolean>

    private var realm : Realm by Delegates.notNull()

    constructor() : super()
    {
        realm = Realm.getDefaultInstance()
        psRealmlIsInserted = PublishSubject.create()
    }

    fun insertAll(httpRcvItemData: HttpRcvItemData, context: Context)
    {
        realm.executeTransactionAsync(
                {   //execute
                    insertDataResult(it, httpRcvItemData)
                }
                ,
                {
                    this.httpRcvItemData = httpRcvItemData
                    Toast.makeText(context,"data is Saved",Toast.LENGTH_SHORT).show()
                    Logger.d("Realm Save Success")
                    psRealmlIsInserted.onNext(true)
                }
                ,
                {
                    Toast.makeText(context,"data save is fail",Toast.LENGTH_SHORT).show()
                    Logger.d("Realm Save fail" + it.printStackTrace())
                    psRealmlIsInserted.onNext(false)
                })
    }

    private fun insertDataResult(realm:Realm, httpRcvItemData: HttpRcvItemData)
    {
        Logger.d("Realm Save execute")
        realm.copyToRealm(httpRcvItemData.info)
        realm.copyToRealmOrUpdate(httpRcvItemData.results)
    }

    fun loadData() : HttpRcvItemData
    {
        var rInfo = realm.where(Info::class.java).findFirst()
        var rResults = realm.where(Result::class.java).findAll()
        Logger.d("Result size " + rResults.size + " rInfo version " + rInfo.version)

        if ( httpRcvItemData == null )
            httpRcvItemData = HttpRcvItemData(rResults, rInfo)
        else
        {
            httpRcvItemData!!.info = rInfo
            httpRcvItemData!!.results = rResults
        }

        return httpRcvItemData!!
    }

    fun delete()
    {
        realm.executeTransaction {
            realm.deleteAll()
            httpRcvItemData = null
        }
    }

    fun isGetData():Boolean
    {
        val resSize = realm.where(Result::class.java).findAll().size
        Logger.d("resSize " + resSize)
        if ( resSize > 0 )
            return true
        return false
    }

    fun onDestroy()
    {
        realm.close()
    }
}