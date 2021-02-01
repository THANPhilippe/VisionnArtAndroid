package com.example.visionnart.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.MainActivity

import com.example.visionnart.R
import com.example.visionnart.dao.OeuvreDao
import com.example.visionnart.models.Exposition
import com.example.visionnart.models.NotationOeuvre
import com.example.visionnart.models.Oeuvre
import com.example.visionnart.utils.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_espace_culturel_info.*
import kotlinx.android.synthetic.main.fragment_oeuvre_info.*
import org.jetbrains.anko.toast

class OeuvreInfoFragment : Fragment {

    private lateinit var auth: FirebaseAuth

    var oeuvre: Oeuvre? = null
    var id: String? = null
    var noteOeuvre: Int = 0
    var moyenne: Float = 0.0f
    var note: NotationOeuvre? = null
    lateinit var notes: MutableList<NotationOeuvre>
    var isMark: Boolean = false

    constructor(oeuvre: Oeuvre?, id: String?) : super() {
        this.oeuvre = oeuvre
        this.id = id
    }

    constructor(id: String) : super() {
        this.id = id
        FirestoreUtil.firestoreInstance.collection("oeuvre").document(id).get().addOnSuccessListener  { document  ->
                if (document != null) {
                    this.oeuvre = document?.toObject(Oeuvre::class.java)
                } else {
                    print("error")
                }
            }.addOnFailureListener { exception ->
            Log.d("ErrorOeuvre", "get failed with ", exception)
        }
        print("oeuvredata "+ oeuvre)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_oeuvre_info, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()



        btnRating.setOnClickListener {
            OeuvreDao.firestoreInstance.collection("notation_oeuvre")
                .whereEqualTo("id_oeuvre", this.id)
                .whereEqualTo("id_user", auth.currentUser?.uid).get()
                .addOnSuccessListener {
                    if (it.toObjects(NotationOeuvre::class.java).size == 0) {
                        val NewNote = hashMapOf(
                            "id_oeuvre" to this.id,
                            "id_user" to auth.currentUser?.uid,
                            "note" to notation.rating
                        )
                    } else {
                        note = it.toObjects(NotationOeuvre::class.java)[0]
                        val NewNote = hashMapOf(
                            "id_oeuvre" to note?.id_oeuvre,
                            "id_user" to note?.id_user,
                            "notation" to notation.rating
                        )
                        OeuvreDao.firestoreInstance.document(it.documents[0].reference.path).set(NewNote)
                    }
                    context?.toast("Votre vote a bien été pris en compte")
                    notation.rating = moyenne
                    animation_validation.playAnimation()
                }
        }


        infoOeuvreName.text = oeuvre?.artwork_name?.get("fr") ?: "Sans nom"
        Picasso.get().load(oeuvre?.img_oeuvre).into(PhotoOeuvre)
        OeuvreDao.firestoreInstance.collection("exposition")
            .document(oeuvre?.id_exposition.toString()).get()
            .addOnSuccessListener { documentSnapshot ->
                infoOeuvreExposition.text =
                    "Exposition : " + documentSnapshot.toObject(Exposition::class.java)?.nom_exposition
            }
        infoOeuvrepropriétaire.text =
            "Musée : " + oeuvre?.owner_name
        infoNomAuteurOeuvre.text =
            "Auteur : " + oeuvre?.author_name
        if(oeuvre?.artwork_description?.get("fr") != "") {
            infoOeuvreDescription.text = "Description : " + oeuvre?.artwork_description?.get("fr")
        }else{
            infoOeuvreDescription.visibility = View.GONE
        }
        if(oeuvre?.url_wikipedia?.get("fr") != null) {
            urlWikipedia.text = "Lien Wikipédia : " + oeuvre?.url_wikipedia?.get("fr")
        }else{
            urlWikipedia.visibility = View.GONE
        }


        /**
         * Retour au fragment des listes
         */

        OeuvreBackToList.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser?.isAnonymous == true || auth.currentUser == null) {
            btnRating.visibility = View.GONE
            notation.visibility = View.GONE
            animation_validation.visibility = View.GONE
        } else {
            OeuvreDao.firestoreInstance.collection("notation_oeuvre")
                .whereEqualTo("id_oeuvre", this.id)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    val notationOeuvre = ArrayList<NotationOeuvre>()
                    noteOeuvre = 0
                    querySnapshot?.documents?.forEach {
                        var notationofoeuvre = it.toObject(NotationOeuvre::class.java)
                        notationOeuvre.add(notationofoeuvre!!)

                        noteOeuvre += notationofoeuvre?.notation!!
                    }
                    if (notationOeuvre.size == 0) {

                    } else {
                        moyenne = (noteOeuvre / notationOeuvre.size).toFloat()
                    }
                    if (isMark) {

                    } else {
                        notation.rating = moyenne
                        isMark = true
                    }

                }
        }
    }
}
