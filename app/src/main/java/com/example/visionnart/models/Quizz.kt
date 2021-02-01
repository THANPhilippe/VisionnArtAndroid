package com.example.visionnart.models

data class Quizz(
    val difficulte_quizz: String?,
    val nom_quizz: String?,
    val id_exposition: String?
) {
    constructor() : this(
        null,
        null,
        null
    )
}