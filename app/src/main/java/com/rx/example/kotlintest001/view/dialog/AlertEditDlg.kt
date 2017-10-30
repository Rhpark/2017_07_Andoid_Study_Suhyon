package com.rx.example.kotlintest001.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.rx.example.kotlintest001.deburg.Logger

/**
 * Created by Rhpark on 2017-10-29.
 */
public class AlertEditDlg
{
    private val activity: AppCompatActivity
    private var alertDlg:AlertDialog.Builder? = null
    private var edtText:EditText? = null
    private var imm: InputMethodManager? = null

    private val btnOk: DialogInterface.OnClickListener
    private val btnCancel: DialogInterface.OnClickListener


    constructor(activity: AppCompatActivity, dataSize:Int, intputType:Int, title:String, subMsg:String,
                btnOk: DialogInterface.OnClickListener, btnCancel: DialogInterface.OnClickListener) {
        this.activity = activity
        this.btnOk = btnOk
        this.btnCancel = btnCancel
        initData(title, subMsg, dataSize,intputType)
    }

    private fun initData(title:String, subMsg:String, dataSize:Int, intputType:Int)
    {
        alertDlg = AlertDialog.Builder(activity)
        alertDlg!!.setTitle(title)
        alertDlg!!.setMessage(subMsg)
        alertDlg!!.setPositiveButton("OK", btnOk)
        alertDlg!!.setNegativeButton("Cancel", btnCancel)

        edtText = EditText(activity)
        edtText!!.setText("" + dataSize)
        edtText!!.requestFocus()
        edtText!!.inputType = intputType
        edtText!!.setSelection(edtText!!.text.toString().length)

        alertDlg!!.setView(edtText)

        imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun getNumber():Int = edtText!!.text.toString().toInt()

    fun isGetNumber():Boolean {
        try
        {
            var number =  edtText!!.text.toString().toInt()
            return true
        }
        catch (e:NumberFormatException)
        {
            Logger.e(e.printStackTrace().toString())
            e.printStackTrace()
            return false
        }
    }

    private fun openKeyboard() = imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

    fun closeKeyboard() = imm!!.hideSoftInputFromWindow(edtText!!.getWindowToken(), 0)

    fun isShowDlg():Boolean = alertDlg!!.create().isShowing

    fun showDlg() {
        alertDlg!!.create().show()
        openKeyboard()
    }
}