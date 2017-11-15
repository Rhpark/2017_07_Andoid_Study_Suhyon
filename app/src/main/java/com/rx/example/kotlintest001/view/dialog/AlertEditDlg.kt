package com.rx.example.kotlintest001.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import io.reactivex.subjects.PublishSubject

/**
 * Created by Rhpark on 2017-10-29.
 */
public class AlertEditDlg
{
    private val activity: AppCompatActivity

    private lateinit var alertDlg:AlertDialog.Builder
    private lateinit var edtText:EditText
    private lateinit var imm: InputMethodManager

    lateinit var rxPsBtnClick: PublishSubject<Boolean>

    constructor(activity: AppCompatActivity, dataSize:Int, inputType:Int, title:String, subMsg:String)
    {
        this.activity = activity
        initData(title, subMsg, dataSize, inputType)
    }

    private fun initData(title:String, subMsg:String, dataSize:Int, inputType:Int)
    {
        rxPsBtnClick = PublishSubject.create()

        alertDlg = AlertDialog.Builder(activity)
        alertDlg.setTitle(title)
        alertDlg.setMessage(subMsg)
        alertDlg.setPositiveButton("OK", DialogInterface.OnClickListener {
            dialogInterface, i -> rxPsBtnClick.onNext(true)
        })
        alertDlg.setNegativeButton("Cancel", DialogInterface.OnClickListener {
            dialogInterface, i -> rxPsBtnClick.onNext(false)
        })

        edtText = EditText(activity)
        edtText.setText("" + dataSize)
        edtText.requestFocus()
        edtText.inputType = inputType
        edtText.setSelection(edtText.text.toString().length)

        alertDlg.setView(edtText)

        imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun getEditNumber():Int = edtText.text.toString().toInt()

    fun isGetEditNumber():Boolean
    {
        try
        {
            edtText.text.toString().toInt()
            return true
        }
        catch (e:NumberFormatException)
        {
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