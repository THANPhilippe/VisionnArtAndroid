package com.example.visionnart.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.visionnart.MainActivity
import com.example.visionnart.R
import com.example.visionnart.dao.OeuvreDao
import com.example.visionnart.models.Oeuvre
import com.example.visionnart.adapters.OeuvreAdapterExtend
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_list_oeuvre.*

class ListOeuvreFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_oeuvre, container, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerListOeuvre.layoutManager = GridLayoutManager(context,3)

        view.apply {
            BackEspace.setOnClickListener {
                (activity as MainActivity).replaceFragment(ExplorerFragment())
            }
        }

        OeuvreDao.firestoreInstance.collection("oeuvre")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val mapOeuvres = LinkedHashMap<Oeuvre, String>()
                val oeuvres = ArrayList<Oeuvre>()
                querySnapshot?.documents?.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid) {
                        mapOeuvres[it.toObject(Oeuvre::class.java)!!] = it?.id.toString()
                        oeuvres.add(it.toObject(Oeuvre::class.java)!!)

                    }
                }

                val adapter = OeuvreAdapterExtend(oeuvres)
                recyclerListOeuvre.adapter = adapter
                adapter.setOnItemClickListener(object : OeuvreAdapterExtend.OnItemClickListener {
                    override fun onItemClick(oeuvre: Oeuvre) {
                        (activity as MainActivity).replaceFragment(
                            OeuvreInfoFragment(
                                oeuvre,
                                mapOeuvres[oeuvre]
                            )
                        )
                    }

                })

            }
    }


}