package com.example.visionnart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.models.EspaceCulturel
import com.example.visionnart.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_espaceculturelextends.view.*
import java.util.*

class EspaceCulturelAdapterExtend(val espaceCulturels: ArrayList<EspaceCulturel>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<EspaceCulturelAdapterExtend.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var mOnItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspaceCulturelAdapterExtend.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_espaceculturelextends, parent, false)
        context = v.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: EspaceCulturelAdapterExtend.ViewHolder, position: Int) {
        holder.bindItems(espaceCulturels[position])
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(espaceCulturel: EspaceCulturel) {
            itemView.setOnClickListener { mOnItemClickListener.onItemClick(espaceCulturel) }
            Picasso.get().load(espaceCulturel.image_espace_culturel).into(itemView.Photoextends)
            itemView.textNameextends.text = espaceCulturel.nom_espace_culturel
//            espaceCulturel.id_type_espace_culturel?.get()?.addOnSuccessListener { documentSnapshot ->
//                val typeEspaceCulturel = documentSnapshot.toObject(TypeEspaceCulturel::class.java)
//                itemView.textDescription.text = typeEspaceCulturel?.nom_type_espace_culturel ?: ""
//            }
            itemView.textDescriptionextends.text = espaceCulturel.description_espace_culturel
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
