package com.psygon.tech.scholar.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.holders.SummaryHolder
import com.psygon.tech.scholar.models.ExamModel

class SummaryAdapter(
    val activity: Activity,
    val itemList: MutableList<ExamModel>
) :
    RecyclerView.Adapter<SummaryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return if (viewType == 1)
            SummaryHolder(layoutInflater.inflate(R.layout.custom_summary_right, parent, false))
        else
            SummaryHolder(layoutInflater.inflate(R.layout.custom_summary_wrong, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        val examModel = itemList[position]

        return if (examModel.isCorrect)
            1
        else 0
    }

    override fun onBindViewHolder(holder: SummaryHolder, position: Int) {
        val examModel = itemList[position]

        val questionString = "Q: ${examModel.content.question}"
        holder.tvQuestion.text = questionString

        var answerString = examModel.givenAnswer
        answerString = answerString.replace(";", ", ")
        answerString = "A: $answerString"
        holder.tvAnswer.text = answerString

        val verdictString = if (examModel.isCorrect) {
            "Correct."
        } else {
            var correctAnswerString = examModel.content.answer
            correctAnswerString = correctAnswerString.replace(";", ", ")

            "Wrong. Correct: $correctAnswerString"
        }
        holder.tvVerdict.text = verdictString
    }
}