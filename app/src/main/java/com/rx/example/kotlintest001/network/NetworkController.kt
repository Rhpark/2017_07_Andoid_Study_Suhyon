package com.rx.example.kotlintest001.network

//import android.webkit.CookieManager
import android.content.Context
import android.net.ConnectivityManager
import com.rx.example.kotlintest001.deburg.Logger
import com.rx.example.kotlintest001.network.http.HttpRcvMain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import android.content.Context.CONNECTIVITY_SERVICE
import kotlin.properties.Delegates


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
        val networkCheck = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        Logger.d()
        if ( networkCheck == null)
            return false

        val wifi    = networkCheck.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile  = networkCheck.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

        Logger.d("mobile "+mobile.isConnected +" ,wifi "+ wifi.isConnected)

        if ( false == mobile.isConnected && false == wifi.isConnected)
            return false
        return true
    }
}
