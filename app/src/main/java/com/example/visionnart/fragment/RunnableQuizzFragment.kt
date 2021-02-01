package com.example.visionnart.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.visionnart.MainActivity
import com.example.visionnart.R
import com.example.visionnart.adapters.QuizzAdapter
import com.example.visionnart.dao.QuizzDao
import com.example.visionnart.models.Exposition
import com.example.visionnart.models.Quizz
import com.example.wivart.fragments.QuestionsFragment
import kotlinx.android.synthetic.main.fragment_runnable_quizz.*


class RunnableQuizzFragment : Fragment {

    constructor() : super()

    private var exposition: Exposition? = null
    private var id: String? = null

    constructor(exposition: Exposition, id: String?) : super() {

        this.exposition = exposition
        this.id = id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_runnable_quizz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerRunnableQuizz.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        QuizzDao.firestoreInstance.collection("quizz").whereEqualTo("id_exposition", this.id)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val mapQuizz = LinkedHashMap<Quizz, String>()
                val quizzs = ArrayList<Quizz>()
                querySnapshot?.documents?.forEach {
                    mapQuizz[it.toObject(Quizz::class.java)!!] = it?.id.toString()
                    quizzs.add(it.toObject(Quizz::class.java)!!)
                }
                if (quizzs.size > 0) {
                    noQuizz.visibility = View.GONE
                    val adapter = QuizzAdapter(quizzs)
                    recyclerRunnableQuizz.adapter = adapter
                    adapter.setOnItemClickListener(object : QuizzAdapter.OnItemClickListener {
                        override fun onItemClick(quizz: Quizz) {
                            (activity as MainActivity).replaceFragment(
                                QuestionsFragment(
                                    quizz,
                                    mapQuizz[quizz]
                                )
                            )
                        }
                    })
                }
                else {

                }

            }
        expoName.text = "Bienvenue dans les quizz de l'exposition"
        quizzName.text = exposition?.nom_exposition
    }
}
