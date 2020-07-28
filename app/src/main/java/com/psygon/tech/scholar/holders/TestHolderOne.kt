package com.psygon.tech.scholar.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R

class TestHolderOne(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvQuestion = itemView.findViewById<TextView>(R.id.tvQuestion)
    val etAnswer = itemView.findViewById<TextView>(R.id.etAnswer)
}