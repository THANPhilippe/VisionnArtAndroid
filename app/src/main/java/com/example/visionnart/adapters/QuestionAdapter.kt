package com.example.visionnart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.R
import com.example.visionnart.models.QuestionQuizz
import kotlinx.android.synthetic.main.item_question.view.*
import java.util.*

class QuestionAdapter(private val questionQuizzs: ArrayList<QuestionQuizz>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var mOnItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        context = v.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: QuestionAdapter.ViewHolder, position: Int) {
        holder.bindItems(questionQuizzs[position])
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(questionQuizz: QuestionQuizz) {
            itemView.setOnClickListener { mOnItemClickListener.onItemClick(questionQuizz) }
            itemView.question.text = questionQuizz.intitule_question
        }
    }

    interface OnItemClickListener {
        fun onItemClick(questionQuizz: QuestionQuizz)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return questionQuizzs.size
    }

}
