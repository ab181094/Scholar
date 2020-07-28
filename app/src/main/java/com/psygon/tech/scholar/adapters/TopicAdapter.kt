package com.psygon.tech.scholar.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.activities.SubjectsActivity
import com.psygon.tech.scholar.activities.TopicsActivity
import com.psygon.tech.scholar.helpers.toUppercase
import com.psygon.tech.scholar.holders.TopicHolder
import com.psygon.tech.scholar.models.Topic

class TopicAdapter(
    val activity: Activity,
    val itemList: MutableList<Topic>
) :
    RecyclerView.Adapter<TopicHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TopicHolder(layoutInflater.inflate(R.layout.custom_topic, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: TopicHolder, position: Int) {
        val topic = itemList[position]

        holder.tvKeyword.text = toUppercase(topic.name.substring(0, 1))
        holder.tvName.text = topic.name

        holder.itemView.setOnClickListener {
            if (activity is TopicsActivity) {
                activity.showChapters(position)
            }
        }

        holder.imgBtnMore.setOnClickListener {
            showPopupMore(it, topic)
        }
    }

    private fun showPopupMore(view: View?, topic: Topic) {
        PopupMenu(activity, view).apply {
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_update -> {
                        if (activity is TopicsActivity) {
                            activity.updateTopics(topic)
                        }
                        true
                    }

                    R.id.action_remove -> {
                        if (activity is TopicsActivity) {
                            activity.removeTopics(topic)
                        }
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.menu_topic)
            show()
        }
    }
}