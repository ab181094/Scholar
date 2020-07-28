package com.psygon.tech.scholar.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.activities.SubjectsActivity
import com.psygon.tech.scholar.helpers.toUppercase
import com.psygon.tech.scholar.holders.SubjectHolder
import com.psygon.tech.scholar.models.Subject

class SubjectAdapter(
    val activity: Activity,
    val itemList: MutableList<Subject>
) :
    RecyclerView.Adapter<SubjectHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SubjectHolder(layoutInflater.inflate(R.layout.custom_subject, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SubjectHolder, position: Int) {
        val subject = itemList[position]

        holder.tvKeyword.text = toUppercase(subject.name.substring(0, 1))
        holder.tvName.text = subject.name

        holder.itemView.setOnClickListener {
            if (activity is SubjectsActivity) {
                activity.showChapters(position)
            }
        }

        holder.imgBtnMore.setOnClickListener {
            showPopupMore(it, subject)
        }
    }

    private fun showPopupMore(
        view: View?,
        subject: Subject
    ) {
        PopupMenu(activity, view).apply {
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_update -> {
                        if (activity is SubjectsActivity) {
                            activity.updateSubject(subject)
                        }
                        true
                    }

                    R.id.action_remove -> {
                        if (activity is SubjectsActivity) {
                            activity.removeSubject(subject)
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