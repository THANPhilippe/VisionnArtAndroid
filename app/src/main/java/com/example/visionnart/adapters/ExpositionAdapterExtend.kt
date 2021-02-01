package com.example.visionnart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.R
import com.example.visionnart.models.Exposition
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_expositionextends.view.*
import java.util.*

class ExpositionAdapterExtend(val expositions: ArrayList<Exposition>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ExpositionAdapterExtend.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var mOnItemClickListener: ExpositionAdapterExtend.OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpositionAdapterExtend.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_expositionextends, parent, false)
        context = v.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ExpositionAdapterExtend.ViewHolder, position: Int) {
        holder.bindItems(expositions[position])
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(exposition: Exposition) {
            itemView.setOnClickListener { mOnItemClickListener.onItemClick(exposition) }
            Picasso.get().load(exposition.img_exposition).into(itemView.Photoexpoextend)
            itemView.itemExpositionNameextend.text = exposition.nom_exposition
            itemView.itemExpositionDescriptionextend.text = exposition.description_exposition
        }
    }

    interface OnItemClickListener {
        fun onItemClick(exposition: Exposition)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return expositions.size
    }

}