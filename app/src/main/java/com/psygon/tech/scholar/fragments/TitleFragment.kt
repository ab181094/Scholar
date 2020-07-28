package com.psygon.tech.scholar.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.psygon.tech.scholar.R

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {
    private lateinit var tvTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_title, container, false)

        tvTitle = rootView.findViewById(R.id.tvTitle)

        return rootView
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }
}
