package com.rx.example.kotlintest001.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.rx.example.kotlintest001.deburg.Logger
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

/**
 * Created by Rhpark on 2017-10-29.
 */
public class AlertEditDlg
{
    private val activity: AppCompatActivity

    private var alertDlg:AlertDialog.Builder by Delegates.notNull()
    private var edtText:EditText by Delegates.notNull()
    private var imm: InputMethodManager by Delegates.notNull()

    lateinit var psBtnClick: PublishSubject<Boolean>

    constructor(activity: AppCompatActivity, dataSize:Int, intputType:Int, title:String, subMsg:String)
    {
        this.activity = activity
        initData(title, subMsg, dataSize,intputType)
    }


    private fun initData(title:String, subMsg:String, dataSize:Int, intputType:Int)
    {
        psBtnClick = PublishSubject.create()

        alertDlg = AlertDialog.Builder(activity)
        alertDlg.setTitle(title)
        alertDlg.setMessage(subMsg)
        alertDlg.setPositiveButton("OK", DialogInterface.OnClickListener {
            dialogInterface, i -> psBtnClick.onNext(true)
        })
        alertDlg.setNegativeButton("Cancel", DialogInterface.OnClickListener {
            dialogInterface, i -> psBtnClick.onNext(false)
        })

        edtText = EditText(activity)
        edtText.setText("" + dataSize)
        edtText.requestFocus()
        edtText.inputType = intputType
        edtText.setSelection(edtText.text.toString().length)

        alertDlg.setView(edtText)

        imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun getNumber():Int = edtText.text.toString().toInt()

    fun isGetNumber():Boolean
    {
        try
        {
            edtText.text.toString().toInt()
            return true
        }
        catch (e:NumberFormatException)
        {
            Logger.e(e.printStackTrace().toString())
            e.printStackTrace()
            return false
        }
    }

    private fun openKeyboard() = imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

    fun closeKeyboard() = imm.hideSoftInputFromWindow(edtText.getWindowToken(), 0)

    fun isShowDlg():Boolean = alertDlg.create().isShowing

    fun showDlg()
    {
        alertDlg.create().show()
        openKeyboard()
    }
}