package com.rx.example.kotlintest001.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.model.http.dao.Result
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

/**
 */
public class AdapterRcvMain : RecyclerView.Adapter<AdapterRcvMainViewHolder> {

    /*한번에 추가로 보여줄수 있는 갯수 20*/
    companion object {  val MAX_ADD_VALUE = 20  }

    var listSize = 0

    var clickEvent = PublishSubject.create<Int>()
    lateinit var results:MutableList<Result>   //get total data

    constructor() : super()

    fun getItem(position:Int):Result = results.get(position)

    fun addItemListSize()
    {
        val Totalsize = results.size
        if ( Totalsize > MAX_ADD_VALUE )
            listSize = MAX_ADD_VALUE
        else
            listSize = Totalsize
    }

    override fun getItemCount() = listSize

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AdapterRcvMainViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_main_rcv, parent, false)

        val viewholder = AdapterRcvMainViewHolder(view, clickEvent)
//        clickEvent = viewholder.clickSubject
        return viewholder
    }

    override fun onBindViewHolder(holder: AdapterRcvMainViewHolder?, position: Int)
    {
        holder?.bind(position
                ,results.get(position)?.name!!.fullName()
                ,results.get(position)?.gender!!
                ,results.get(position)?.picture?.large!!)
    }
}