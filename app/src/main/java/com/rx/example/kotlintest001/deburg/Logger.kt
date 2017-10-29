package com.rx.example.kotlintest001.deburg

import android.util.Log

/**
 */
object Logger {

    private val INCLUDE_TAGNAME = 0
    private val UNINCLUDE_TAGNAME = -1        //아무것도 보여주지 않는다.그래서 return 에서만 사용.
    private val DETATIVE_IS_MY_TAGNAME = 1

    internal var IS_DEBUG = true
    private val IS_MY_TAG = false    //특정 TAG 설정한 것만 보여주고 싶을 때 1(1개 혹은 그이상도 설정가능하게 하기)

    private val MY_TAG_NAME_ARY = arrayOf("NetworkController", "AdapterRcvMain")//Tag Filter using -> IS_MY_TAG = true

    private val EMPTYDATA = " Rhpark"

    /**
     * @return EMPTYDATA > simpleClassName > methodName
     */
    private val tag: String
        get() {
            val ste = Thread.currentThread().stackTrace[4]
            val sp = ste.className.split(".")
            return EMPTYDATA + " > "+sp.get(sp.size-1) + " > " + ste.methodName
        }

    /**
     * @param msg
     * @return String(# 라인 수 >  msg );
     */
    private fun buildMessage(msg: String?): String {

        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder()

        sb.append("#")
        sb.append(ste.lineNumber)

        if (msg != null) {
            sb.append(" > ")
            sb.append(msg)
        }

        return sb.toString()
        //return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg);
    }

    private fun IsMyTagCheck(Tag: String): Boolean {
        for (i in MY_TAG_NAME_ARY.indices) {
            if (MY_TAG_NAME_ARY[i] == Tag == true)
                return true
        }
        return false
    }

    private fun isLogcatWrite(tagNum: Int): Boolean {
        if ( false == IS_DEBUG ) return false

        if (tagNum == UNINCLUDE_TAGNAME) return false

        return true
    }

    /**
     * check Tag value(MY_TAG 가 TRUE 인지, TAG가 포한되어있는지.
     *
     * @param Tag
     * @return
     */
    private fun contianValue(Tag: String): Int {
        return if (IS_MY_TAG == true) {
            if (IsMyTagCheck(Tag) == true) INCLUDE_TAGNAME else UNINCLUDE_TAGNAME

        } else DETATIVE_IS_MY_TAGNAME
    }

    /*****Log.v */
    fun v() {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.v(tag , buildMessage(null))
    }

    fun v(msg: String) {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.v(tag , buildMessage(msg))
    }

    fun v(Tag: String, msg: String?) {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.v(Tag + tag , buildMessage(msg))
    }

    /*****Log.e */
    fun e(msg: String) {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.e(tag , buildMessage(msg))
    }

    fun e(Tag: String, msg: String) {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.e(Tag + tag , buildMessage(msg))
    }

    /*****Log.i */
    fun i() {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.i(tag , buildMessage(null))
    }

    fun i(msg: String) {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.i(tag , buildMessage(msg))
    }

    fun i(Tag: String, msg: String?) {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.i(Tag + tag , buildMessage(msg))
    }

    /*****Log.w */
    fun w() {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.w(tag , buildMessage(null))
    }

    fun w(msg: String) {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.w(tag , buildMessage(msg))
    }

    fun w(Tag: String, msg: String?) {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.w(Tag + tag , buildMessage(msg))
    }

    /*****Log.d */
    fun d() {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.d(tag , buildMessage(null))
    }

    fun d(msg: String)
    {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.d(tag , buildMessage(msg))
    }

    fun d(Tag: String, msg: String?)
    {
        if ( false == isLogcatWrite( contianValue( tag ))) return

        Log.d(Tag + tag , buildMessage(msg))
    }
}
