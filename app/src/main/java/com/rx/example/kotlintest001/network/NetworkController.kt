package com.rx.example.kotlintest001.network

import android.content.Context
import android.net.ConnectivityManager
import com.rx.example.kotlintest001.deburg.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit

/**
 */

public class NetworkController
{
    private val READ_TIMEOUT = 10L
    private val WRITE_TIMEOUT = 10L
    private val CONNECT_TIMEOUT = 10L

    private val HTTP_URL = "https://randomuser.me/"//"https://randomuser.me/api/?results=20"

    private var context :Context

    lateinit private var client : Retrofit

    lateinit private var okHttpClient : OkHttpClient

    constructor(context: Context)
    {
        this.context = context
        initData()
    }

    private fun initData()
    {
        var logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        CookieManager().setCookiePolicy (CookiePolicy.ACCEPT_ALL)

        okHttpClient = OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

        client = Retrofit.Builder()
                .baseUrl(HTTP_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun <T>getHttpService( httpServices : Class< T > ) : T = client.create(httpServices)

    fun isNetworkCheck():Boolean
    {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.activeNetworkInfo?.let {
            if (true == it.isConnected)
            {
                Logger.d("NetworkType " + it.type)
                return (it.type == ConnectivityManager.TYPE_WIFI || it.type == ConnectivityManager.TYPE_MOBILE)
            }
        }
        return false
    }
}
