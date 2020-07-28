package com.psygon.tech.scholar.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.activities.HomeActivity
import com.psygon.tech.scholar.holders.MenuHolder
import com.psygon.tech.scholar.models.HomeMenu

class HomeAdapter(
    val activity: Activity,
    val itemList: MutableList<HomeMenu>
) :
    RecyclerView.Adapter<MenuHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MenuHolder(layoutInflater.inflate(R.layout.custom_menu_home, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MenuHolder, position: Int) {
        val homeMenu = itemList[position]

        holder.imageView.setImageResource(homeMenu.image)
        holder.tvName.text = homeMenu.name

        holder.itemView.setOnClickListener {
            if (activity is HomeActivity) {
                activity.getMenuAction(homeMenu.name)
            }
        }
    }
}