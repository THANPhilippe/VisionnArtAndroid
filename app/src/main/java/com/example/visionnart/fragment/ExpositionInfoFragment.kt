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
import com.example.visionnart.adapters.OeuvreAdapter
import com.example.visionnart.dao.ExpositionDao
import com.example.visionnart.dao.OeuvreDao
import com.example.visionnart.models.*
import com.example.visionnart.utils.DateUtil
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_exposition_info.*
import org.jetbrains.anko.toast


@SuppressLint("ValidFragment")
class ExpositionInfoFragment : Fragment {

    private lateinit var auth: FirebaseAuth

    constructor() : super() {}

    /**
     * Objet Exposition
     */
    var exposition: Exposition? = null

    /**
     * Id de l exposition
     */
    var id: String? = null

    var nomTypeExpo: String? = null

    constructor(exposition: Exposition, id: String?) : super() {
        this.exposition = exposition
        this.id = id
    }

    constructor(id: String) : super() {
        this.id = id
        ExpositionDao.firestoreInstance.collection("exposition").document(id)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                this.exposition = documentSnapshot?.toObject(Exposition::class.java)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exposition_info, container, false)


    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val glm = GridLayoutManager(activity, 3)
        recyclerOeuvreExpoInfo.layoutManager = glm

        infoExpoName.text = exposition?.nom_exposition ?: "Sans nom"
        infoExpoDescription.text = "Description : " + exposition?.description_exposition
        infoExpoLocalisation.text = "Localisation : " + exposition?.localisation_exposition
        Picasso.get().load(exposition?.img_exposition).into(Photoexpo2)
        ExpositionDao.firestoreInstance.collection("espace_culturel")
            .document(exposition?.id_espace_culturel.toString()).get().addOnSuccessListener { documentSnapshot ->
                infoExpoEspaceCulturel.text =
                    "Espace Culturel : " + documentSnapshot.toObject(EspaceCulturel::class.java)?.nom_espace_culturel
                val idTypeExpo = documentSnapshot.toObject(EspaceCulturel::class.java)?.id_type_espace_culturel
                ExpositionDao.firestoreInstance.collection("type_espace_culturel").document(idTypeExpo!!)
                    .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                        this.nomTypeExpo = documentSnapshot?.toObject(TypeEspaceCulturel::class.java)?.nom_type_espace_culturel
                    }
            }
        ExpositionDao.firestoreInstance.collection("periode").document(exposition?.id_periode_exposition.toString())
            .get().addOnSuccessListener { documentSnapshot ->
                infoExpoPeriode.text = "Periode : " + documentSnapshot.toObject(Periode::class.java)?.nom_periode
            }

        ExpositionDao.firestoreInstance.collection("theme_exposition")
            .document(exposition?.id_theme_exposition.toString()).get().addOnSuccessListener { documentSnapshot ->
                infoExpoTheme.text =
                    "Theme : " + documentSnapshot.toObject(ThemeExposition::class.java)?.nom_theme_exposition
            }



        infoExpoDateDebut.text =
            "Date du début de l'exposition : " + DateUtil.formatdate(exposition?.date_debut_exposition?.toDate())

        infoExpoDateFin.text =
            "Date du fin de l'exposition : " + DateUtil.formatdate(exposition?.date_fin_exposition?.toDate())




        OeuvreDao.firestoreInstance.collection("oeuvre").whereEqualTo("id_exposition", this.id)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                val mapoeuvres = LinkedHashMap<Oeuvre, String>()
                val oeuvres = ArrayList<Oeuvre>()
                querySnapshot?.documents?.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid) {
                        if(mapoeuvres.size != 9){
                            mapoeuvres[it.toObject(Oeuvre::class.java)!!] = it?.id.toString()
                            oeuvres.add(it.toObject(Oeuvre::class.java)!!)
                        }
                    }
                }

                val adapter = OeuvreAdapter(oeuvres)
                recyclerOeuvreExpoInfo.adapter = adapter
                adapter.setOnItemClickListener(object : OeuvreAdapter.OnItemClickListener {
                    override fun onItemClick(oeuvre: Oeuvre) {
                        (activity as MainActivity).replaceFragment(
                            OeuvreInfoFragment(
                                oeuvre,
                                mapoeuvres[oeuvre]
                            ))
                    }

                })


            }

        /**
         * Retour au fragment des listes
         */

        createExpoBackToListExpo.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        /**
         * Bouton quizz
         */
        infoExpButtonQuizz.setOnClickListener {

            (activity as MainActivity).replaceFragment(RunnableQuizzFragment(
                this.exposition!!,
                this.id))

        }

    }
}
