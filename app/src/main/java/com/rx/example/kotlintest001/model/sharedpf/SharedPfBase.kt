package com.rx.example.kotlintest001.model.sharedpf

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Rhpark on 2017-10-29.
 */
public open abstract class SharedPfBase {

    protected val context:Context
    protected open var sp:SharedPreferences? = null
    protected open var editor:SharedPreferences.Editor? = null

    constructor(context: Context)
    {
        this.context = context
    }

    protected open fun openInt(key:String, defaultValue:Int):Int = sp!!.getInt(key,defaultValue)

    protected open fun openFloat(key:String, defaultValue:Float):Float = sp!!.getFloat(key,defaultValue)

    protected open fun openLong(key:String, defaultValue:Long):Long = sp!!.getLong(key,defaultValue)

    protected open fun openBoolean(key:String, defaultValue:Boolean):Boolean = sp!!.getBoolean(key,defaultValue)

    protected open fun openString(key:String, defaultValue:String):String = sp!!.getString(key,defaultValue)

    protected open fun saveInt(key:String, value:Int) {
        editor = sp!!.edit()
        editor!!.putInt(key,value)
        editor!!.commit()
    }

    protected open fun saveLong(key:String, value:Long) {
        editor = sp!!.edit()
        editor!!.putLong(key,value)
        editor!!.commit()
    }

    protected open fun saveBoolean(key:String, value:Boolean) {
        editor = sp!!.edit()
        editor!!.putBoolean(key,value)
        editor!!.commit()
    }

    protected open fun saveInt(key:String, value:Float) {
        editor = sp!!.edit()
        editor!!.putFloat(key,value)
        editor!!.commit()
    }

    protected open fun saveString(key:String, value:String) {
        editor = sp!!.edit()
        editor!!.putString(key,value)
        editor!!.commit()
    }
}