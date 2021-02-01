package com.example.visionnart.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.visionnart.MainActivity

import com.example.visionnart.R
import kotlinx.android.synthetic.main.fragment_scan.view.*

class ScanFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_scan, container, false)

        view.apply {
            scanningBtn.setOnClickListener {
                (activity as MainActivity).toCamera()
            }
        }
        return view
    }

}
