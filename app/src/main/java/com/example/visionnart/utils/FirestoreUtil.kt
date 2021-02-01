package com.example.visionnart.utils

import com.example.visionnart.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object FirestoreUtil {

    public val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document(
            "users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null.")}"
        )

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapShot ->
            if (!documentSnapShot.exists()) {
                val newUser = User(
                    "https://firebasestorage.googleapis.com/v0/b/visonnart.appspot.com/o/Img%2FProfilPicture%2FNo_person-1.jpg?alt=media&token=43421780-2347-49cb-a8cc-d2820f319b58",
                    FirebaseAuth.getInstance().currentUser?.email ?: "",
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    FirebaseAuth.getInstance().currentUser?.phoneNumber ?: "",
                    "Homme",
                    "",
                    ""
                )
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else {
                onComplete()
            }
        }
    }

    fun updateImageUser(
        image: String = ""
    ) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (image.isNotBlank()) {
            userFieldMap["image"] = image
        }
        currentUserDocRef.update(userFieldMap)
    }

    fun updateCurrentUser(
        email: String = "",
        name: String = "",
        phone: String = "",
        gender: String = "",
        age: String = "",
        town: String = ""
    ) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (email.isNotBlank()) {
            userFieldMap["email"] = email
        }
        if (name.isNotBlank()) {
            userFieldMap["name"] = name
        }
        if (phone.isNotBlank()) {
            userFieldMap["phone"] = phone
        }
        if (gender.isNotBlank()) {
            userFieldMap["gender"] = gender
        }
        if (age.isNotBlank()) {
            userFieldMap["age"] = age
        }
        if (town.isNotBlank()) {
            userFieldMap["town"] = town
        }
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User?) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(User::class.java))
            }
    }
}
