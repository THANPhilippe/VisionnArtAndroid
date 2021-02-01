package com.example.visionnart.models

data class NotationOeuvre (
    val id_oeuvre : String?,
    val id_user : String?,
    val notation : Int?
) {
    constructor() : this(
        null,
        null,
        null
    )
}