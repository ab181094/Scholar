package com.psygon.tech.scholar.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.activities.ChaptersActivity
import com.psygon.tech.scholar.activities.TopicsActivity
import com.psygon.tech.scholar.helpers.toUppercase
import com.psygon.tech.scholar.holders.ChapterHolder
import com.psygon.tech.scholar.models.Chapter

class ChapterAdapter(
    val activity: Activity,
    val itemList: MutableList<Chapter>
) :
    RecyclerView.Adapter<ChapterHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChapterHolder(layoutInflater.inflate(R.layout.custom_chapter, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ChapterHolder, position: Int) {
        val chapter = itemList[position]

        holder.tvKeyword.text = toUppercase(chapter.name.substring(0, 1))
        holder.tvName.text = chapter.name

        holder.itemView.setOnClickListener {
            if (activity is ChaptersActivity) {
                activity.showContents(position)
            }
        }

        holder.imgBtnMore.setOnClickListener {
            showPopupMore(it, chapter)
        }
    }

    private fun showPopupMore(view: View?, chapter: Chapter) {
        PopupMenu(activity, view).apply {
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_update -> {
                        if (activity is ChaptersActivity) {
                            activity.updateChapters(chapter)
                        }
                        true
                    }

                    R.id.action_remove -> {
                        if (activity is ChaptersActivity) {
                            activity.removeChapters(chapter)
                        }
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.menu_chapter)
            show()
        }
    }
}