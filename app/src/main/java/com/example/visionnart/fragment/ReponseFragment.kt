package com.example.visionnart.fragments


import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.visionnart.MainActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.visionnart.R
import com.example.visionnart.adapters.ReponseAdapter
import com.example.visionnart.dao.ExpositionDao
import com.example.visionnart.dao.QuizzDao
import com.example.visionnart.models.QuestionQuizz
import com.example.visionnart.models.QuestionQuizzUtilisateur
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_reponse.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*


class ReponseFragment : Fragment {

    constructor() : super() {}

    /**
     * Objet Question_Quizz
     */
    var question: QuestionQuizz? = null

    /**
     * Id de l'objet Question_Quizz
     */
    var id: String? = null

    /**
     * id du quizz
     */
    var id_quizz : String? = null

    private var iddoc : String? = null
    private var mapquizz : String = ""

    /**
     * si la réponse est correct
     */
    var is_correct: Boolean? = null
    var already: Boolean = false

    @SuppressLint("ValidFragment")
    constructor(question: QuestionQuizz, id: String?, id_quizz: String?) : super() {
        this.question = question
        this.id = id
        this.id_quizz = id_quizz
    }

    @SuppressLint("ValidFragment")
    constructor(id: String?) : super() {
        this.id = id
        ExpositionDao.firestoreInstance.collection("question_quizz").document(id.toString())
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                this.question = documentSnapshot?.toObject(QuestionQuizz::class.java)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reponse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValuesFields()
        reclyclerResponse.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        val mapReponsesToNum: LinkedHashMap<String?, Int> = initMapReponse()
        val mapNumToReponse: LinkedHashMap<Int, String?> = LinkedHashMap()
        var AnimationView: LottieAnimationView = view.findViewById(R.id.reponseImg) as LottieAnimationView

        AnimationView.addAnimatorListener(
            object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }
                override fun onAnimationEnd(animation: Animator) {
                }
                override fun onAnimationCancel(animation: Animator) {
                }
                override fun onAnimationRepeat(animation: Animator) {
                    AnimationView.pauseAnimation()
                }
            })

        AnimationView.visibility = View.INVISIBLE

        for (i in 0..(question?.reponses?.size!! - 1)) {
            mapNumToReponse[i] = question?.reponses?.get(i)
        }

        QuizzDao.firestoreInstance.collection("question_quizz_utilisateur").whereEqualTo("id_question", id)
            .whereEqualTo("id_utilisateur", FirebaseAuth.getInstance().currentUser?.uid).get()
            .addOnSuccessListener { documents ->
                val listQuestionQuizzUtilisateur = ArrayList<QuestionQuizzUtilisateur?>()
                for (document in documents) {
                    mapquizz = document?.id.toString()
                    listQuestionQuizzUtilisateur.add(document.toObject(QuestionQuizzUtilisateur::class.java))
                    already = true
                }
                val adapter = ReponseAdapter(question?.reponses, mapNumToReponse[question?.correct], already)
                reclyclerResponse.adapter = adapter
                adapter.setOnItemClickListener(object : ReponseAdapter.OnItemClickListener {
                    override fun onItemClick(reponse: String?) {
                        if (mapReponsesToNum.containsKey(reponse)) {
                            is_correct = if (mapReponsesToNum[reponse] == question?.correct) {
                                lottieDisplay(AnimationView, R.raw.check_orange)
                                repQuestion("Bonne réponse !", mapReponsesToNum[reponse]!!)
                                true
                            } else {
                                lottieDisplay(AnimationView, R.raw.unapproved_cross)
                                repQuestion("Mauvaise réponse !", mapReponsesToNum[reponse]!!)
                                false
                            }
                        }

                    }

                })

            }
    }

    private fun lottieDisplay(_loLottieAnimationView: LottieAnimationView, _rawRes: Int) {
        _loLottieAnimationView.visibility = View.VISIBLE
        _loLottieAnimationView.setAnimation(_rawRes)
        _loLottieAnimationView.playAnimation()
    }

    private fun repQuestion(message: String, num_reponse: Int) {
        context?.alert(message) {
            yesButton {
                if (!already) {
                    QuizzDao.addQuestionQuizzUtilisateur(
                        QuestionQuizzUtilisateur(
                            id,
                            FirebaseAuth.getInstance().currentUser!!.uid,
                            true,
                            num_reponse,
                            is_correct,
                            id_quizz
                        )
                    )
                } else if (already) {
                    QuizzDao.updateQuestionQuizzUtilisateur(mapquizz,
                        QuestionQuizzUtilisateur(
                            id,
                            FirebaseAuth.getInstance().currentUser!!.uid,
                            true,
                            num_reponse,
                            is_correct,
                            id_quizz
                        )
                    )
                }

                (activity as MainActivity).onBackPressed()
            }
        }?.show()
    }

    private fun initMapReponse(): LinkedHashMap<String?, Int> {
        val map: LinkedHashMap<String?, Int> = LinkedHashMap()
        for (i in 0..(question?.reponses?.size!! - 1)) {
            map[question?.reponses?.get(i)] = i
        }
        return map
    }


    private fun initValuesFields() {
        quizzName.text = question?.intitule_question ?: "Pas de question"
    }


}