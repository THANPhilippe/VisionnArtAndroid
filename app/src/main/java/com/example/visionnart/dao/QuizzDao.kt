package com.example.visionnart.dao

import com.example.visionnart.models.QuestionQuizzUtilisateur
import com.example.visionnart.models.Quizz
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object QuizzDao {

    val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private var table = "quizz"

    fun getQuizz(id:String) {
        val docRef = firestoreInstance.collection("quizz").document(id)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val exposition = documentSnapshot.toObject(Quizz::class.java)
        }
    }

    fun addQuizz(quizz: Quizz) {
        ExpositionDao.firestoreInstance.collection(table).document().set(quizz)
    }

    fun addQuestionQuizzUtilisateur(questionQuizzUtilisateur: QuestionQuizzUtilisateur) {
        ExpositionDao.firestoreInstance.collection("question_quizz_utilisateur").add(questionQuizzUtilisateur)
    }

    fun updateQuestionQuizzUtilisateur(path: String,questionQuizzUtilisateur: QuestionQuizzUtilisateur) {
        ExpositionDao.firestoreInstance.document("question_quizz_utilisateur/$path").set(questionQuizzUtilisateur)
    }

    fun deleteQuizz(id: String?) {
        EspaceCulturelDao.firestoreInstance.collection(table).document(id.toString()).delete()
    }


    fun getInstance() :FirebaseFirestore{
        return firestoreInstance
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()
}
