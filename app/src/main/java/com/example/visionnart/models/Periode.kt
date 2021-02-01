package com.example.visionnart.models

data class Periode(
    val id_periode: String,
    val nom_periode: String
) {
    constructor() : this(
        "",
        ""
    )

}