package com.example.visionnart.dao

import com.example.visionnart.models.EspaceCulturel
import com.example.visionnart.models.Exposition
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object ExpositionDao {

    val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private var table = "exposition"

    fun getExposition(id:String) {
        val docRef = firestoreInstance.collection("exposition").document(id)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val exposition = documentSnapshot.toObject(EspaceCulturel::class.java)
        }
    }

    fun addExposition(exposition: Exposition) {
        ExpositionDao.firestoreInstance.collection(table).document().set(exposition)
    }

    fun deleteExposition(id: String?) {
        EspaceCulturelDao.firestoreInstance.collection(table).document(id.toString()).delete()
    }


    fun getInstance() :FirebaseFirestore{
        return firestoreInstance
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()
}
