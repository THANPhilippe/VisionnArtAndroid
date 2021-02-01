package com.example.visionnart.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.R
import kotlinx.android.synthetic.main.item_question.view.*

class ReponseAdapter(
    private val list: List<String>?,
    private val reponse: String?,
    val isAlreadyPlay: Boolean
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ReponseAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var mOnItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReponseAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        context = v.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ReponseAdapter.ViewHolder, position: Int) {
        holder.bindItems(list?.get(position))
        if (reponse.equals(list?.get(position)) && isAlreadyPlay) {
            holder.itemView.setBackgroundColor(Color.parseColor("#167158"))
        }
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(questionQuizz: String?) {
            itemView.setOnClickListener {
                mOnItemClickListener.onItemClick(questionQuizz)
                it.setBackgroundColor(Color.parseColor("#AC2FBE"))
            }
            itemView.question.text = questionQuizz
        }
    }

    interface OnItemClickListener {
        fun onItemClick(reponse: String?)

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

}
