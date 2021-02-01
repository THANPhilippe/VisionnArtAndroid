package com.example.visionnart.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.visionnart.MainActivity
import com.example.visionnart.R
import com.example.visionnart.dao.EspaceCulturelDao
import com.example.visionnart.models.EspaceCulturel
import com.example.visionnart.adapters.EspaceCulturelAdapterExtend
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_listespace.*

class ListEspaceCulturelFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listespace, container, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerEspaceCulturel2.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        view.apply {
            BackEspace.setOnClickListener {
                (activity as MainActivity).replaceFragment(ExplorerFragment())
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

                val adapter = EspaceCulturelAdapterExtend(espaceCulturels)
                recyclerEspaceCulturel2.adapter = adapter
                adapter.setOnItemClickListener(object : EspaceCulturelAdapterExtend.OnItemClickListener {
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
    }


}