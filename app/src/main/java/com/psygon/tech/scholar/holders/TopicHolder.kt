package com.psygon.tech.scholar.holders

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R

class TopicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvKeyword = itemView.findViewById<TextView>(R.id.tvLogo)
    val tvName = itemView.findViewById<TextView>(R.id.tvName)
    val imgBtnMore = itemView.findViewById<ImageButton>(R.id.imgBtnMore)
}