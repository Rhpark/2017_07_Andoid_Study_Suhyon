package com.rx.example.kotlintest001.model.realm.dto

import android.content.Context
import android.widget.Toast
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.model.http.dao.Info
import com.rx.example.kotlintest001.model.http.dao.Result
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlin.properties.Delegates

/**
 * Created by Rhpark on 2017-10-30.
 */
public class RealmHttpRcvDTO
{
    private var realm : Realm by Delegates.notNull()
//    private var realmConfig : RealmConfiguration by Delegates.notNull()

    public constructor() : super()
    {
        initData()
    }

    fun initData()
    {
//        realmConfig = RealmConfiguration.Builder().build()
//        Realm.deleteRealm(realmConfig)
//        realm = Realm.getInstance(realmConfig)

        realm = Realm.getDefaultInstance()
    }

    fun insertAll(httpRcvItemData: HttpRcvItemData, context: Context)
    {
        realm.executeTransactionAsync(
                {
                    insertDataResult(it, httpRcvItemData.results!!, httpRcvItemData.info!!)
                }
                ,
                {
                    Toast.makeText(context,"data is Saved",Toast.LENGTH_SHORT).show()
                    Logger.d("Realm Save Success")
                }
                ,
                {
                    Toast.makeText(context,"data save is fail",Toast.LENGTH_SHORT).show()
                    Logger.d("Realm Save fail" + it.printStackTrace())
                })
    }

    fun insertDataResult(realm:Realm, results:MutableList<Result>, info:Info)
    {
        Logger.d("Realm Save execute")
        realm.copyToRealm(info)
        realm.copyToRealmOrUpdate(results)
    }

    fun getData() : HttpRcvItemData
    {
        var rResults = realm.where(Result::class.java).findAll()
        var rInfo = realm.where(Info::class.java).findFirst()
        Logger.d("Result size " + rResults.size + " rInfo version " + rInfo.version)

        return HttpRcvItemData(rResults , rInfo)
    }

    fun delete() {
        realm.executeTransaction {

            realm.delete(Result::class.java)
            realm.delete(Info::class.java)
        }
    }

    fun isGetData():Boolean
    {
        val resSize = realm.where(Result::class.java).findAll().size
        Logger.d("resSize " + resSize)
        if ( resSize > 0 ) return true
        return false
    }

    fun onDestroy()
    {
        realm.close()
    }
}