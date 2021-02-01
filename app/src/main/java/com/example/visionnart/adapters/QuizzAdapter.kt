package com.example.visionnart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.R
import com.example.visionnart.dao.QuizzDao
import com.example.visionnart.models.DifficulteQuizz
import com.example.visionnart.models.Quizz
import kotlinx.android.synthetic.main.item_quizz.view.*
import java.util.*

class QuizzAdapter(private val listQuizzs: ArrayList<Quizz>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<QuizzAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var mOnItemClickListener: QuizzAdapter.OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_quizz, parent, false)
        context = v.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: QuizzAdapter.ViewHolder, position: Int) {
        holder.bindItems(listQuizzs[position])
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindItems(simpleQuizz: Quizz) {
            itemView.setOnClickListener { mOnItemClickListener.onItemClick(simpleQuizz) }
            itemView.quizzText.text = simpleQuizz.nom_quizz
            QuizzDao.firestoreInstance.collection("difficulte_quizz")
                .document(simpleQuizz.difficulte_quizz.toString()).get().addOnSuccessListener { documentSnapshot ->
                    itemView.quizzDifficulty.text =
                        documentSnapshot.toObject(DifficulteQuizz::class.java)?.nom_difficulte_quizz
                }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(quizz: Quizz)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return listQuizzs.size
    }

}
