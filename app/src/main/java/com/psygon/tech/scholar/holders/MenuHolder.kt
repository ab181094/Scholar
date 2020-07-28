package com.psygon.tech.scholar.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R
import de.hdodenhof.circleimageview.CircleImageView

class MenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView = itemView.findViewById<CircleImageView>(R.id.itemImage)
    val tvName = itemView.findViewById<TextView>(R.id.tvName)
}