package com.example.visionnart.models

data class QuestionQuizz(
    val intitule_question: String?,
    val id_quizz: String?,
    val reponses: List<String>?,
    val correct: Int?
) {
    constructor() : this(
        null,
        null,
        null,
        null
    )
}