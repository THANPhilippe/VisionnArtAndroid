package com.example.visionnart.models

import com.google.firebase.Timestamp

data class Exposition(
    val nom_exposition: String?,
    val id_espace_culturel:String?,
    val id_theme_exposition: String?,
    val id_periode_exposition:String?,
    val description_exposition: String?,
    val localisation_exposition : String?,
    val date_debut_exposition : Timestamp?,
    val date_fin_exposition: Timestamp?,
    val id_type_exposition:String?,
    val img_exposition:String?
) {
    constructor():this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}
