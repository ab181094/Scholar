package com.psygon.tech.scholar.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R

class SummaryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvQuestion = itemView.findViewById<TextView>(R.id.tvQuestion)
    val tvAnswer = itemView.findViewById<TextView>(R.id.tvAnswer)
    val tvVerdict = itemView.findViewById<TextView>(R.id.tvVerdict)
}