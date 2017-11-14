package com.rx.example.kotlintest001.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_main_rcv.view.*

class AdapterRcvMainViewHolder(itemView: View, clickSubject : PublishSubject<Int>)
    : RecyclerView.ViewHolder(itemView)
{
    init {
        itemView.setOnClickListener {
            clickSubject.onNext(layoutPosition)
        }
    }

    fun bind(count:Int,name:String,gender:String, imgUrl:String)
    {
        itemView.tvCount.setText("No."+ count )
        itemView.tvName.setText( name )
        itemView.tvGender.setText( gender)
        Glide.with(itemView).load( imgUrl).into(itemView.ivPicture)
    }
}