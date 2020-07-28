package com.psygon.tech.scholar.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.activities.ContentsActivity
import com.psygon.tech.scholar.helpers.MY_LIST_PATH
import com.psygon.tech.scholar.helpers.toast
import com.psygon.tech.scholar.holders.ContentHolderFour
import com.psygon.tech.scholar.holders.ContentHolderOne
import com.psygon.tech.scholar.holders.ContentHolderTwo
import com.psygon.tech.scholar.models.Content

class MyListAdapter(
    val activity: Activity,
    val itemList: MutableList<Content>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> ContentHolderOne(
                layoutInflater.inflate(
                    R.layout.custom_view_content_one,
                    parent,
                    false
                )
            )
            2 -> ContentHolderTwo(
                layoutInflater.inflate(
                    R.layout.custom_view_content_two,
                    parent,
                    false
                )
            )
            else -> ContentHolderFour(
                layoutInflater.inflate(
                    R.layout.custom_view_content_four,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        val content = itemList[position]
        return content.noOfOptions
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val content = itemList[position]

        when (holder) {
            is ContentHolderOne -> {
                val questionString = "Q: ${content.question}"
                holder.tvQuestion.text = questionString

                val answerString = "A: ${content.answer}"
                holder.tvAnswer.text = answerString

                holder.imgBtnMore.setOnClickListener {
                    showPopupMore(it, content)
                }
            }

            is ContentHolderTwo -> {
                val questionString = "Q: ${content.question}"
                holder.tvQuestion.text = questionString

                holder.checkBoxOne.text = content.option1
                holder.checkBoxTwo.text = content.option2

                holder.checkBoxOne.isClickable = false
                holder.checkBoxTwo.isClickable = false

                if (content.answer.contains(";")) {
                    holder.checkBoxOne.isChecked = true
                    holder.checkBoxTwo.isChecked = true
                } else {
                    if (content.answer == content.option1) {
                        holder.checkBoxOne.isChecked = true
                    } else {
                        holder.checkBoxTwo.isChecked = true
                    }
                }

                holder.imgBtnMore.setOnClickListener {
                    showPopupMore(it, content)
                }
            }

            is ContentHolderFour -> {
                val questionString = "Q: ${content.question}"
                holder.tvQuestion.text = questionString

                holder.checkBoxOne.text = content.option1
                holder.checkBoxTwo.text = content.option2
                holder.checkBoxThree.text = content.option3
                holder.checkBoxFour.text = content.option4

                holder.checkBoxOne.isClickable = false
                holder.checkBoxTwo.isClickable = false
                holder.checkBoxThree.isClickable = false
                holder.checkBoxFour.isClickable = false

                val answerList = mutableListOf<String>()

                if (content.answer.contains(";")) {
                    val answerString = content.answer
                    val parts = answerString.split(";")

                    for (part in parts) {
                        answerList.add(part)
                    }
                } else {
                    answerList.add(content.answer)
                }

                for (answer in answerList) {
                    when (answer) {
                        content.option1 -> holder.checkBoxOne.isChecked = true
                        content.option2 -> holder.checkBoxTwo.isChecked = true
                        content.option3 -> holder.checkBoxThree.isChecked = true
                        else -> holder.checkBoxFour.isChecked = true
                    }
                }

                holder.imgBtnMore.setOnClickListener {
                    showPopupMore(it, content)
                }
            }
        }
    }

    private fun showPopupMore(
        view: View,
        content: Content
    ) {
        PopupMenu(activity, view).apply {
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.action_add -> {
                        if (activity is ContentsActivity) {
                            activity.addToMyList(content)
                        }
                        true
                    }

                    R.id.action_remove -> {
                        if (activity is ContentsActivity) {
                            activity.removeContent(content)
                        }
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.menu_my_list)
            show()
        }
    }
}