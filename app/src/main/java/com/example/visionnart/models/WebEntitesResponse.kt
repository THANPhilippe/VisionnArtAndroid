package com.example.visionnart.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

class WebEntitesResponse() {
    data class Base(val responses: List<Responses>?)

    data class BestGuessLabels(val label: String?)

    data class FullMatchingImages(val url: String?)

    data class PagesWithMatchingImages(val url: String?, val pageTitle: String?, val partialMatchingImages: List<PartialMatchingImages>?)

    data class PartialMatchingImages(val url: String?)

    data class Responses(val webDetection: WebDetection?){
        class Deserializer: ResponseDeserializable<Array<Responses>> {
            override fun deserialize(content: String): Array<Responses>? = Gson().fromJson(content, Array<Responses>::class.java)
        }
    }

    data class VisuallySimilarImages(val url: String?)

    data class WebDetection(val webEntities: List<WebEntities>?, val fullMatchingImages: List<FullMatchingImages>?, val partialMatchingImages: List<PartialMatchingImages>?, val pagesWithMatchingImages: List<PagesWithMatchingImages>?, val visuallySimilarImages: List<VisuallySimilarImages>?, val bestGuessLabels: List<BestGuessLabels>?)

    data class WebEntities(val entityId: String?, val score: Number?, val description: String?)

}
