package com.example.wivart.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.visionnart.MainActivity
import com.example.visionnart.R
import com.example.visionnart.adapters.QuestionAdapter
import com.example.visionnart.dao.QuizzDao
import com.example.visionnart.fragments.ReponseFragment
import com.example.visionnart.models.DifficulteQuizz
import com.example.visionnart.models.QuestionQuizz
import com.example.visionnart.models.QuestionQuizzUtilisateur
import com.example.visionnart.models.Quizz
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_questions.*


class QuestionsFragment : Fragment {

    constructor() : super() {}

    /**
     * Objet Quizz
     */
    var quizz: Quizz? = null

    /**
     * Id de l exposition
     */
    var id: String? = null

    /**
     * Compteur de question
     */
    var nb_question: Int? = 0

    @SuppressLint("ValidFragment")
    constructor(quizz: Quizz, id: String?) : super() {
        this.quizz = quizz
        this.id = id
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValuesFields()
        reclyclerQuestion.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        Log.d("id", "" + id)
        QuizzDao.firestoreInstance.collection("question_quizz").whereEqualTo("id_quizz", id.toString()).get()
            .addOnSuccessListener { querySnapshot ->
                val mapQuestionQuizz = LinkedHashMap<QuestionQuizz, String>()
                val questionsQuizz = ArrayList<QuestionQuizz>()
                querySnapshot?.documents?.forEach {
                    mapQuestionQuizz[it.toObject(QuestionQuizz::class.java)!!] = it?.id.toString()
                    questionsQuizz.add(it.toObject(QuestionQuizz::class.java)!!)
                }
                nb_question = questionsQuizz.size

                val adapter = QuestionAdapter(questionsQuizz)
                reclyclerQuestion.adapter = adapter
                adapter.setOnItemClickListener(object : QuestionAdapter.OnItemClickListener {
                    override fun onItemClick(questionQuizz: QuestionQuizz) {
                        (activity as MainActivity).replaceFragment(
                            ReponseFragment(
                                questionQuizz,
                                mapQuestionQuizz[questionQuizz],
                                id
                            )
                        )
                    }

                })

            }
        /**
         * Count Correct Answer and number of question
         */
        val id_user = (FirebaseAuth.getInstance()).currentUser!!.uid
        QuizzDao.firestoreInstance.collection("question_quizz_utilisateur")
            .whereEqualTo("id_utilisateur", id_user)
            .whereEqualTo("id_quizz", id)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val mapQuestionQuizzU = LinkedHashMap<QuestionQuizzUtilisateur, String>()
                val questionsQuizzU = ArrayList<QuestionQuizzUtilisateur>()
                querySnapshot?.documents?.forEach {
                    mapQuestionQuizzU[it.toObject(QuestionQuizzUtilisateur::class.java)!!] = it?.id.toString()
                    questionsQuizzU.add(it.toObject(QuestionQuizzUtilisateur::class.java)!!)
                }
                var nbPlayed = 0
                var nbCorrect = 0
                questionsQuizzU.forEach() {
                    if (it.alreadyPlay!!) {
                        nbPlayed++
                        if (it._correct!!)
                            nbCorrect++
                    }
                }
                val nbfaute = nbPlayed!! - nbCorrect
                nbQuestion.text = "Question Répondu : $nbPlayed / $nb_question"
                nbCorrectAnswer.text = "Vous avez actuellement $nbfaute faute."


            }

    }


    private fun initValuesFields() {
        quizzName.text = quizz?.nom_quizz ?: "Sans nom"
        QuizzDao.firestoreInstance.collection("difficulte_quizz")
            .document(quizz?.difficulte_quizz.toString()).get().addOnSuccessListener { documentSnapshot ->
                questionName.text = documentSnapshot.toObject(DifficulteQuizz::class.java)?.nom_difficulte_quizz
            }
    }


}