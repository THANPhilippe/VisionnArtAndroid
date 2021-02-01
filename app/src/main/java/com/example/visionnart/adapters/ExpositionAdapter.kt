package com.example.visionnart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.R
import com.example.visionnart.models.Exposition
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_exposition.view.*
import java.util.ArrayList

class ExpositionAdapter(val expositions: ArrayList<Exposition> ):
    androidx.recyclerview.widget.RecyclerView.Adapter<ExpositionAdapter.ViewHolder>(){

    private lateinit var context: Context
    private lateinit var mOnItemClickListener: ExpositionAdapter.OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpositionAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_exposition, parent, false)
        context = v.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ExpositionAdapter.ViewHolder, position: Int) {
        holder.bindItems(expositions[position])
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(exposition: Exposition) {
            itemView.setOnClickListener { mOnItemClickListener.onItemClick(exposition) }
            Picasso.get().load(exposition.img_exposition).into(itemView.Photoexpo)
            itemView.itemExpositionName.text = exposition.nom_exposition
            itemView.itemExpositionDescription.text = exposition.description_exposition
        }
    }

    interface OnItemClickListener {
        fun onItemClick(exposition: Exposition)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int { return expositions.size }

}