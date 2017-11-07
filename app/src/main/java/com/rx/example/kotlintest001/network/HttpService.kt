package com.rx.example.kotlintest001.network

import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.model.realm.dto.RealmHttpRcvDTO
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query
/**
 */
public interface HttpService
{
    @GET("api/")
    fun getDatList( @Query("results")  count : Int ) : Call<HttpRcvItemData>
}

