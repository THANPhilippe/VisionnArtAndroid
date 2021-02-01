package com.example.wivart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.R
import com.example.visionnart.models.EspaceCulturel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_espaceculturel.view.*
import java.util.*

class EspaceCulturelAdapter(val espaceCulturels: ArrayList<EspaceCulturel>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<EspaceCulturelAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var mOnItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspaceCulturelAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_espaceculturel, parent, false)
        context = v.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: EspaceCulturelAdapter.ViewHolder, position: Int) {
            holder.bindItems(espaceCulturels[position])
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(espaceCulturel: EspaceCulturel) {
            itemView.setOnClickListener { mOnItemClickListener.onItemClick(espaceCulturel) }
            Picasso.get().load(espaceCulturel.image_espace_culturel).into(itemView.Photo)
            itemView.textName.text = espaceCulturel.nom_espace_culturel
            itemView.textDescription.text = espaceCulturel.description_espace_culturel
        }
    }

    interface OnItemClickListener {
        fun onItemClick(espaceCulturel: EspaceCulturel)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return espaceCulturels.size
    }

}
