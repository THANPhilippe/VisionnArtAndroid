package com.example.visionnart.dao

import com.example.visionnart.models.Oeuvre
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object OeuvreDao {

    val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun getoeuvre(id: String) {
        val docRef = firestoreInstance.collection("oeuvre").document(id)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val oeuvre = documentSnapshot.toObject(Oeuvre::class.java)
        }
    }

    fun getInstance(): FirebaseFirestore {
        return firestoreInstance
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()
}
