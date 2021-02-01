package com.example.visionnart.models

class WebEntitiesRequest(var imageUrl: String, var type: String, var maxResults: Int) {
    private var base:Base = Base(listOf(
        Requests(
            Image(
                Source(imageUrl)
            ), listOf(
                Features(type,maxResults))
        ),Requests(
            Image(
                Source(imageUrl)
            ), listOf(
                Features(type,maxResults))
        )))

    public fun getRequests(): Any{
        return base
    }

    data class Base(val requests: List<Requests>?)

    data class Features(val type: String?, val maxResults: Number?)

    data class Image(val source: Source?)

    data class Requests(val image: Image?, val features: List<Features>?)

    data class Source(val imageUri: String?)

}

