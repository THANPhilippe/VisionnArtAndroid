package com.example.visionnart.models

data class EspaceCulturel(
    val description_espace_culturel: String?,
    val id_adresse_espace_culturel: String?,
    val id_type_espace_culturel: String?,
    val nom_espace_culturel: String?,
    val image_espace_culturel: String?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null
    )
}
