package com.example.visionnart.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.visionnart.MainActivity
import com.example.visionnart.R
import com.example.visionnart.dao.EspaceCulturelDao
import com.example.visionnart.models.*
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_espace_culturel_info.*


@SuppressLint("ValidFragment")
class EspaceCulturelInfoFragment : Fragment {
    private var isMark: Boolean = false
    private lateinit var auth: FirebaseAuth
    constructor() : super() {}

    /**
     * Objet Espace Culturel
     */
    var espaceculturel: EspaceCulturel? = null

    /**
     * Id de l Espace Culturel
     */
    var id: String? = null

    constructor(espaceCulturel: EspaceCulturel, id: String?) : super() {
        this.espaceculturel = espaceCulturel
        this.id = id
    }

    constructor(id: String) : super() {
        this.id = id
        EspaceCulturelDao.firestoreInstance.collection("espace_culturel").document(id)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                this.espaceculturel = documentSnapshot?.toObject(EspaceCulturel::class.java)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_espace_culturel_info, container, false)


    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        var name = espaceculturel?.nom_espace_culturel
        infoEspaceCultName.text = espaceculturel?.nom_espace_culturel ?: "Sans nom"
        Picasso.get().load(espaceculturel?.image_espace_culturel).into(PhotoEspaceCult2)
        EspaceCulturelDao.firestoreInstance.collection("type_espace_culturel")
            .document(espaceculturel?.id_type_espace_culturel.toString()).get()
            .addOnSuccessListener { documentSnapshot ->
                infoEspaceCultType.text =
                    "Type Espace Culturel : " + documentSnapshot.toObject(TypeEspaceCulturel::class.java)?.nom_type_espace_culturel
            }
        infoEspaceCultDescription.text =
            "Description : " + espaceculturel?.description_espace_culturel ?: "pas de description"

        /**
         * Retour au fragment des listes
         */

        createEspaceCultBackToListEspaceCult.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }
}
