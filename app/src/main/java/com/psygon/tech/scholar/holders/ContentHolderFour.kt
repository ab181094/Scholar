package com.psygon.tech.scholar.holders

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R

class ContentHolderFour(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvQuestion = itemView.findViewById<TextView>(R.id.tvQuestion)
    val checkBoxOne = itemView.findViewById<CheckBox>(R.id.checkBoxOption1)
    val checkBoxTwo = itemView.findViewById<CheckBox>(R.id.checkBoxOption2)
    val checkBoxThree = itemView.findViewById<CheckBox>(R.id.checkBoxOption3)
    val checkBoxFour = itemView.findViewById<CheckBox>(R.id.checkBoxOption4)
    val imgBtnMore = itemView.findViewById<ImageButton>(R.id.imgBtnMore)
}