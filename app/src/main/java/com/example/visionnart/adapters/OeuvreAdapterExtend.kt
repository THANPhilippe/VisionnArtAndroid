package com.example.visionnart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.R
import com.example.visionnart.models.Oeuvre
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_oeuvres.view.*
import kotlinx.android.synthetic.main.item_oeuvresextends.view.*
import java.util.*

class OeuvreAdapterExtend(val oeuvres: ArrayList<Oeuvre>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<OeuvreAdapterExtend.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var mOnItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_oeuvresextends, parent, false)
        context = v.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(oeuvres[position])
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(oeuvre: Oeuvre) {
            itemView.setOnClickListener { mOnItemClickListener.onItemClick(oeuvre) }
            Picasso.get().load(oeuvre.img_oeuvre).into(itemView.Photooeuvresextend)
            val size = oeuvre.artwork_name?.get("fr")?.length
            var name = oeuvre.artwork_name?.get("fr")
            if(size != null){
                if(size > 30){
                    name = oeuvre.artwork_name?.get("fr")?.substring(0,20)+"..."
                }
            }
            itemView.itemOeuvreNameextend.text = name
        }
    }

    interface OnItemClickListener {
        fun onItemClick(oeuvre: Oeuvre)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return oeuvres.size
    }

}