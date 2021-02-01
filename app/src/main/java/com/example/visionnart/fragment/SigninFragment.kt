package com.example.visionnart.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.visionnart.MainActivity

import com.example.visionnart.R
import kotlinx.android.synthetic.main.fragment_sign_in.view.*

class SigninFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        view.apply {
            connexion.setOnClickListener {
                (activity as MainActivity).connection()
            }
        }
        return view
    }

}
