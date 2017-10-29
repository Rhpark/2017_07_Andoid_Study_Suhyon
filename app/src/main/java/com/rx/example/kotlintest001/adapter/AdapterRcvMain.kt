package com.rx.example.kotlintest001.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.model.http.HttpRcvItemData

/**
 */
public class AdapterRcvMain : RecyclerView.Adapter<AdapterRcvMain.ViewHolder> {

    companion object {
        val MAX_ADD_VALUE = 20  //한번에 추가로 보여줄수 있는 갯수 20
    }
    val context : Context

    var httpRcvItemData : HttpRcvItemData? = null   //total data

    var listSize = 0

    constructor(context: Context ) : super() {
        this.context = context
    }

    fun listSizeCheck()
    {
        val Totalsize = httpRcvItemData!!.results.size
        if ( Totalsize > MAX_ADD_VALUE )
            listSize = MAX_ADD_VALUE
        else
            listSize = Totalsize
    }

    override fun getItemCount() = listSize

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AdapterRcvMain.ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)
    {
        holder!!.tvCount.setText("No."+ position )
        holder!!.tvName.setText( httpRcvItemData!!.results.get( position ).name.getFullName() )
        holder!!.tvAge.setText( httpRcvItemData!!.results.get( position ).gender)
        Glide.with(context).load( httpRcvItemData!!.results.get( position ).picture.large).into(holder!!.ivPicture)
    }

    open inner class ViewHolder(parent: ViewGroup?)
        : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_rcv,parent,false))
    {
        var ivPicture   : ImageView
        var tvName      : TextView
        var tvAge       : TextView
        var tvCount     : TextView

        init {
            ivPicture = itemView.findViewById(R.id.ivPicture) as ImageView
            tvName = itemView.findViewById(R.id.tvName) as TextView
            tvAge = itemView.findViewById(R.id.tvAge) as TextView
            tvCount = itemView.findViewById(R.id.tvCount) as TextView
        }
    }
}