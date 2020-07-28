package com.psygon.tech.scholar.holders

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R

class ContentHolderOne(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvQuestion = itemView.findViewById<TextView>(R.id.tvQuestion)
    val tvAnswer = itemView.findViewById<TextView>(R.id.tvAnswer)
    val imgBtnMore = itemView.findViewById<ImageButton>(R.id.imgBtnMore)
}