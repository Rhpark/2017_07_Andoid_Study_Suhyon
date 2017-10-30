package com.rx.example.kotlintest001.model.sharedpf

import android.content.Context

/**
 * Created by Rhpark on 2017-10-29.
 */
public class SharedPfRcvMainDataSize : SharedPfBase
{
    private val KEY_DATA_TABLE = "KEY_DATA_TABLE"
    private val KEY_DATA_SIZE = "KEY_DATA_SIZE"
    private val DEFAULT_VALUE = 5000

    public var dataSize:Int? = null

    constructor(context: Context) : super(context)
    {
        sp = context.getSharedPreferences(KEY_DATA_TABLE, Context.MODE_PRIVATE)
    }

    public fun openDataSize() {
        dataSize = openInt(KEY_DATA_SIZE,DEFAULT_VALUE)
    }

    public fun saveDataSize(dataValue:Int) {
        dataSize = dataValue
        saveInt(KEY_DATA_SIZE,dataValue)
    }
}