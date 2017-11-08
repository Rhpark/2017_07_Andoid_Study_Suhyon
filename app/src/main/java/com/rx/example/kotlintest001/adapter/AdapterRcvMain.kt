package com.rx.example.kotlintest001.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.model.http.dao.Result
import io.reactivex.subjects.PublishSubject

/**
 */
class AdapterRcvMain : RecyclerView.Adapter<AdapterRcvMainViewHolder> {

    /*한번에 추가로 보여줄수 있는 갯수 20*/
    companion object {  val MAX_ADD_VALUE = 20  }

    var listSize: Int
    var psRcvItemSelected: PublishSubject<Int>

    lateinit var results:MutableList<Result>   //get total data

    constructor() : super()
    {
        psRcvItemSelected = PublishSubject.create()
        listSize = 0
    }

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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AdapterRcvMainViewHolder
    {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_main_rcv, parent, false)

        return AdapterRcvMainViewHolder(view, psRcvItemSelected)
    }

    override fun onBindViewHolder(holder: AdapterRcvMainViewHolder?, position: Int)
    {
        holder?.bind(position
                ,results.get(position).name!!.fullName()
                ,results.get(position).gender!!
                ,results.get(position).picture?.large!!)
    }
}