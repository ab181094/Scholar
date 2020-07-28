package com.psygon.tech.scholar.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.activities.SubjectsActivity
import com.psygon.tech.scholar.activities.ViewTestActivity
import com.psygon.tech.scholar.holders.ViewTestHolder
import com.psygon.tech.scholar.models.Subject
import com.psygon.tech.scholar.models.Test

class ViewTestAdapter(
    val activity: Activity,
    val itemList: MutableList<Test>
) :
    RecyclerView.Adapter<ViewTestHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewTestHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewTestHolder(layoutInflater.inflate(R.layout.custom_view_test, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewTestHolder, position: Int) {
        val test = itemList[position]
        holder.tvName.text = test.name

        holder.itemView.setOnClickListener {
            if (activity is ViewTestActivity) {
                activity.showTest(test)
            }
        }

        holder.imgBtnMore.setOnClickListener {
            showPopupMore(it, test)
        }
    }

    private fun showPopupMore(
        view: View?,
        test: Test
    ) {
        PopupMenu(activity, view).apply {
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_update -> {
                        if (activity is ViewTestActivity) {
                            activity.updateTest(test)
                        }
                        true
                    }

                    R.id.action_remove -> {
                        if (activity is ViewTestActivity) {
                            activity.removeTest(test)
                        }
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.menu_subject)
            show()
        }
    }
}