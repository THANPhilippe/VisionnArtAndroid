package com.example.visionnart.models

data class QuestionQuizzUtilisateur(
    val id_question: String?,
    val id_utilisateur: String?,
    val alreadyPlay: Boolean?,
    val num_reponse: Int?,
    val _correct: Boolean?,
    val id_quizz :String?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null
    )
}