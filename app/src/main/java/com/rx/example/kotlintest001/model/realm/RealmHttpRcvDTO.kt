package com.rx.example.kotlintest001.model.realm.dto

import android.content.Context
import android.widget.Toast
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.model.http.dto.Info
import com.rx.example.kotlintest001.model.http.dto.Result
import com.rx.example.kotlintest001.model.http.dao.HttpRcvItemData
import io.reactivex.subjects.PublishSubject
import io.realm.Realm

/**
 * Created by Rhpark on 2017-10-30.
 */
class RealmHttpRcvDTO
{
    var httpRcvItemData: HttpRcvItemData? = null
    var psRealmlIsInserted: PublishSubject<Boolean>

    private var realm: Realm

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
        var realmInfo = realm.where(Info::class.java).findFirst()
        var realmResults = realm.where(Result::class.java).findAll()

        val realmHttpRcvItemData = HttpRcvItemData(realmResults, realmInfo)
        Logger.d("Result size " + realmResults.size + " rInfo version " + realmInfo.version)

        if ( httpRcvItemData == null )
            httpRcvItemData = realmHttpRcvItemData
        else
        {
            httpRcvItemData?.let {
                it.info = realmInfo
                it.results = realmResults
            }
        }

        return realmHttpRcvItemData
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
        return ( resSize > 0 )
    }

    fun onDestroy()
    {
        realm.close()
    }
}