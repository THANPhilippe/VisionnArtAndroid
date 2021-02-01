package com.example.visionnart.dao


import com.example.visionnart.models.EspaceCulturel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object EspaceCulturelDao {

    val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun getEspaceCulturel(id: String) {
        val docRef = firestoreInstance.collection("espace_culturel").document(id)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val espaceCulturel = documentSnapshot.toObject(EspaceCulturel::class.java)
            }

    }

    fun getInstance(): FirebaseFirestore {
        return firestoreInstance
    }


    fun removeListener(registration: ListenerRegistration) = registration.remove()
}
