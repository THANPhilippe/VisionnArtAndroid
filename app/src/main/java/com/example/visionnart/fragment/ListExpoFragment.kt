package com.example.visionnart.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.visionnart.MainActivity
import com.example.visionnart.R
import com.example.visionnart.adapters.ExpositionAdapterExtend
import com.example.visionnart.dao.ExpositionDao
import com.example.visionnart.models.Exposition
import kotlinx.android.synthetic.main.fragment_listexposition.*

class ListExpoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listexposition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerExposition2.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        view.apply {
            BackEspaceexpo.setOnClickListener {
                (activity as MainActivity).replaceFragment(ExplorerFragment())
            }
        }

        ExpositionDao.firestoreInstance.collection("exposition")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val mapExpo = LinkedHashMap<Exposition, String>()
                val expositions = ArrayList<Exposition>()
                querySnapshot?.documents?.forEach {
                    mapExpo[it.toObject(Exposition::class.java)!!] = it?.id.toString()
                    expositions.add(it.toObject(Exposition::class.java)!!)
                }
                val adapter = ExpositionAdapterExtend(expositions)
                recyclerExposition2.adapter = adapter
                adapter.setOnItemClickListener(object : ExpositionAdapterExtend.OnItemClickListener {
                    override fun onItemClick(exposition: Exposition) {
                        (activity as MainActivity).replaceFragment(
                            ExpositionInfoFragment(
                                exposition,
                                mapExpo.get(exposition)
                            )
                        )
                    }

                })

            }

    }
}


