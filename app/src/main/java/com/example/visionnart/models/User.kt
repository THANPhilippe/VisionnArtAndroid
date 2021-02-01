package com.example.visionnart.models

import android.net.Uri

data class User(
    val image: String,
    val email: String,
    val name: String,
    val phone: String,
    val gender: String,
    val age: String,
    val town: String

) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        ""


    )
}
