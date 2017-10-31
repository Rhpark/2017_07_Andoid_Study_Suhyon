package com.rx.example.kotlintest001.adapter

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rx.example.kotlintest001.R
import com.rx.example.kotlintest001.model.http.dto.HttpRcvItemData
import com.rx.example.kotlintest001.model.http.dao.Result
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 */
public class AdapterRcvMain : RecyclerView.Adapter<AdapterRcvMain.ViewHolder> {

    companion object {
        val MAX_ADD_VALUE = 20  //한번에 추가로 보여줄수 있는 갯수 20
    }
    private val context : Context
    private val clickSubject = PublishSubject.create<Result>()

    val clickEvent: Observable<Result> = clickSubject
    var listSize = 0
    var selectPosition = 0

    var httpRcvItemData : HttpRcvItemData? = null   //get total data

    constructor(context: Context ) : super() {
        this.context = context
    }

    fun listSizeCheck()
    {
        val Totalsize = httpRcvItemData!!.results!!.size
        if ( Totalsize > MAX_ADD_VALUE )
            listSize = MAX_ADD_VALUE
        else
            listSize = Totalsize
    }

    override fun getItemCount() = listSize

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_main_rcv, parent, false)
        return ViewHolder(view)
//        AdapterRcvMain.ViewHolder = ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)
    {
        holder!!.bind(position
                ,httpRcvItemData!!.results!!.get(position).name!!.fullName()
                ,httpRcvItemData!!.results!!.get(position).gender!!
                ,httpRcvItemData!!.results!!.get(position).picture!!.large!!)
    }

    //    open inner class ViewHolder(parent: ViewGroup?)
//        : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_rcv,parent,false))
    open inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView)
    {
        private val ivPicture   by lazy{    itemView.findViewById(R.id.ivPicture) as ImageView }
        private val tvName      by lazy{    itemView.findViewById(R.id.tvName) as TextView }
        private val tvGender    by lazy{    itemView.findViewById(R.id.tvGender) as TextView }
        private val tvCount     by lazy{    itemView.findViewById(R.id.tvCount) as TextView }

        init {

            itemView.setOnClickListener {
                selectPosition = layoutPosition
                clickSubject.onNext(httpRcvItemData!!.results!!.get(layoutPosition))
            }
        }

        fun bind(count:Int,name:String,gender:String, imgUrl:String)
        {
            tvCount.setText("No."+ count )
            tvName.setText( name )
            tvGender.setText( gender)
            Glide.with(context).load( imgUrl).into(ivPicture)
        }
    }
}