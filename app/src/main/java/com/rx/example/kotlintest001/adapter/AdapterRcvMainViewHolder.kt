package com.rx.example.kotlintest001.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rx.example.kotlintest001.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by INNO_14 on 2017-11-01.
 */
// class ViewHolder(parent: ViewGroup?)
//        : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_rcv,parent,false))
class AdapterRcvMainViewHolder(itemView: View, clickSubject : PublishSubject<Int>) : RecyclerView.ViewHolder(itemView)
{
    private val ivPicture   by lazy{    itemView.findViewById(R.id.ivPicture) as ImageView }
    private val tvName      by lazy{    itemView.findViewById(R.id.tvName) as TextView }
    private val tvGender    by lazy{    itemView.findViewById(R.id.tvGender) as TextView }
    private val tvCount     by lazy{    itemView.findViewById(R.id.tvCount) as TextView }

//    val clickSubject = PublishSubject.create<Int>()

    init {
        itemView.setOnClickListener {
            clickSubject.onNext(layoutPosition)
        }
    }

    fun bind(count:Int,name:String,gender:String, imgUrl:String)
    {
        tvCount.setText("No."+ count )
        tvName.setText( name )
        tvGender.setText( gender)
        Glide.with(itemView).load( imgUrl).into(ivPicture)
    }
}