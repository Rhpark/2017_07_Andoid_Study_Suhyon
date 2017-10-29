package com.rx.example.kotlintest001.network

//import android.webkit.CookieManager
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

/**
 */

public class NetworkController
{
    companion object {
        val CONNECT_TIMEOUT = 10L
        val WRITE_TIMEOUT = 10L
        val READ_TIMEOUT = 10L
    }

    val HTTP_URL = "https://randomuser.me/"//"https://randomuser.me/api/?results=20"
    var client : Retrofit
    var okHttpClient : OkHttpClient

    constructor()
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

    fun isNetworkCheck(networkCheck:ConnectivityManager? = null):Boolean
    {
        Logger.d()
        if ( networkCheck == null)
            return false

        val mobile  = networkCheck.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifi    = networkCheck.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        Logger.d("ddd","mobile "+mobile.isConnected +" ,wifi "+ wifi.isConnected)

        if ( false == mobile.isConnected && false == wifi.isConnected)
            return false

        return true
    }

    fun <T>getHttpService( httpServices : Class< T > ) : T = client.create(httpServices)

}
