package com.example.visionnart.models

data class ThemeExposition(
    val id_theme_exposition: String,
    val nom_theme_exposition: String
) {
    constructor() : this(
        "",
        ""
    )

}