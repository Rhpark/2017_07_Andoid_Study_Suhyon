package com.rx.example.kotlintest001

import android.app.Application
import io.realm.Realm

/**
 * Created by Rhpark on 2017-10-31.
 */
public class MyApplication : Application
{
    constructor() : super()


    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}
