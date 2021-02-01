package com.example.visionnart.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.visionnart.MainActivity
import com.example.visionnart.R
import com.example.visionnart.adapters.ExpositionAdapter
import com.example.visionnart.adapters.OeuvreAdapter
import com.example.visionnart.dao.EspaceCulturelDao
import com.example.visionnart.dao.ExpositionDao
import com.example.visionnart.dao.OeuvreDao
import com.example.visionnart.models.EspaceCulturel
import com.example.visionnart.models.Exposition
import com.example.visionnart.models.Oeuvre
import com.example.wivart.adapters.EspaceCulturelAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_explorer.*
import kotlinx.android.synthetic.main.fragment_explorer.view.*


class ExplorerFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explorer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerEspaceCulturel.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerExposition.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerOeuvre.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        view.apply {
            Textespaceplus1.setOnClickListener {
                (activity as MainActivity).replaceFragment(ListEspaceCulturelFragment())
            }
        }
        view.apply {
            Textexpoplus.setOnClickListener {
                (activity as MainActivity).replaceFragment(ListExpoFragment())
            }
        }
        view.apply {
            Textexpoplus2.setOnClickListener {
                (activity as MainActivity).replaceFragment(ListOeuvreFragment())
            }
        }

        EspaceCulturelDao.firestoreInstance.collection("espace_culturel")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val mapespaceCulturels = LinkedHashMap<EspaceCulturel, String>()
                val espaceCulturels = ArrayList<EspaceCulturel>()
                querySnapshot?.documents?.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid) {
                        mapespaceCulturels[it.toObject(EspaceCulturel::class.java)!!] = it?.id.toString()
                        espaceCulturels.add(it.toObject(EspaceCulturel::class.java)!!)

                    }
                }

                val adapter = EspaceCulturelAdapter(espaceCulturels)
                recyclerEspaceCulturel.adapter = adapter
                adapter.setOnItemClickListener(object : EspaceCulturelAdapter.OnItemClickListener {
                    override fun onItemClick(espaceCulturel: EspaceCulturel) {
                        (activity as MainActivity).replaceFragment(
                            EspaceCulturelInfoFragment(
                                espaceCulturel,
                                mapespaceCulturels[espaceCulturel]
                            )
                        )
                    }

                })
            }

        ExpositionDao.firestoreInstance.collection("exposition")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val mapExpo = LinkedHashMap<Exposition, String>()
                val expositions = ArrayList<Exposition>()
                querySnapshot?.documents?.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid && expositions.size < 4) {
                        mapExpo[it.toObject(Exposition::class.java)!!] = it?.id.toString()
                        expositions.add(it.toObject(Exposition::class.java)!!)
                    }
                }

                Log.e("Map", mapExpo.toString())
                Log.e("List", expositions.toString())
                val adapter = ExpositionAdapter(expositions)
                recyclerExposition.adapter = adapter
                adapter.setOnItemClickListener(object : ExpositionAdapter.OnItemClickListener {
                    override fun onItemClick(exposition: Exposition) {
                        (activity as MainActivity).replaceFragment(
                            ExpositionInfoFragment(
                                exposition,
                                mapExpo[exposition]
                            )
                        )
                    }

                })

            }

        OeuvreDao.firestoreInstance.collection("oeuvre")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val mapoeuvres = LinkedHashMap<Oeuvre, String>()
                val oeuvres = ArrayList<Oeuvre>()
                querySnapshot?.documents?.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid && oeuvres.size < 5) {
                        mapoeuvres[it.toObject(Oeuvre::class.java)!!] = it?.id.toString()
                        oeuvres.add(it.toObject(Oeuvre::class.java)!!)
                    }
                }
                val adapter = OeuvreAdapter(oeuvres)
                recyclerOeuvre.adapter = adapter
                adapter.setOnItemClickListener(object : OeuvreAdapter.OnItemClickListener {
                    override fun onItemClick(oeuvre: Oeuvre) {
                        (activity as MainActivity).replaceFragment(
                            OeuvreInfoFragment(
                                oeuvre,
                                mapoeuvres[oeuvre]
                            )
                        )
                    }
                })
            }
    }
}