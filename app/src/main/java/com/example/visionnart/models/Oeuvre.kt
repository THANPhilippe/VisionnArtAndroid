package com.example.visionnart.models

import java.util.*

class Oeuvre(
    val artwork_description: Map<String, String>?,
    val artwork_height: Int?,
    val artwork_name: Map<String, String>?,
    val artwork_width: Int?,
    val author_name: String?,
    val end_creation_date: Date?,
    val id_exposition: String?,
    val id_oeuvre: String?,
    val img_oeuvre: String?,
    val keyword_artiste: String?,
    val keyword_museum: String?,
    val keyword_name: String?,
    val owner_name: String?,
    val url_wikipedia: Map<String, String>?
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
        null,
        null,
        null,
        null,
        null
    )
}