package com.psygon.tech.scholar.adapters

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psygon.tech.scholar.R
import com.psygon.tech.scholar.holders.TestHolderFour
import com.psygon.tech.scholar.holders.TestHolderOne
import com.psygon.tech.scholar.holders.TestHolderTwo
import com.psygon.tech.scholar.models.ExamModel

class TestAdapter(
    val activity: Activity,
    val itemList: MutableList<ExamModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> TestHolderOne(
                layoutInflater.inflate(
                    R.layout.custom_test_one,
                    parent,
                    false
                )
            )
            2 -> TestHolderTwo(
                layoutInflater.inflate(
                    R.layout.custom_test_two,
                    parent,
                    false
                )
            )
            else -> TestHolderFour(
                layoutInflater.inflate(
                    R.layout.custom_test_four,
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
        val examModel = itemList[position]
        return examModel.content.noOfOptions
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val examModel = itemList[position]

        when (holder) {
            is TestHolderOne -> {
                val questionString = "Q: ${examModel.content.question}"
                holder.tvQuestion.text = questionString

                holder.etAnswer.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        examModel.givenAnswer = s.toString().trim()
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }
                })
            }

            is TestHolderTwo -> {
                val questionString = "Q: ${examModel.content.question}"
                holder.tvQuestion.text = questionString

                holder.checkBoxOne.text = examModel.content.option1
                holder.checkBoxTwo.text = examModel.content.option2

                holder.checkBoxOne.setOnCheckedChangeListener { buttonView, isChecked ->
                    examModel.givenAnswer = if (holder.checkBoxOne.isChecked) {
                        addAnswer(examModel.givenAnswer, examModel.content.option1)
                    } else {
                        removeAnswer(examModel.givenAnswer, examModel.content.option1)
                    }
                }

                holder.checkBoxTwo.setOnCheckedChangeListener { buttonView, isChecked ->
                    examModel.givenAnswer = if (holder.checkBoxTwo.isChecked) {
                        addAnswer(examModel.givenAnswer, examModel.content.option2)
                    } else {
                        removeAnswer(examModel.givenAnswer, examModel.content.option2)
                    }
                }
            }

            is TestHolderFour -> {
                val questionString = "Q: ${examModel.content.question}"
                holder.tvQuestion.text = questionString

                holder.checkBoxOne.text = examModel.content.option1
                holder.checkBoxTwo.text = examModel.content.option2
                holder.checkBoxThree.text = examModel.content.option3
                holder.checkBoxFour.text = examModel.content.option4

                holder.checkBoxOne.setOnCheckedChangeListener { buttonView, isChecked ->
                    examModel.givenAnswer = if (holder.checkBoxOne.isChecked) {
                        addAnswer(examModel.givenAnswer, examModel.content.option1)
                    } else {
                        removeAnswer(examModel.givenAnswer, examModel.content.option1)
                    }
                }

                holder.checkBoxTwo.setOnCheckedChangeListener { buttonView, isChecked ->
                    examModel.givenAnswer = if (holder.checkBoxTwo.isChecked) {
                        addAnswer(examModel.givenAnswer, examModel.content.option2)
                    } else {
                        removeAnswer(examModel.givenAnswer, examModel.content.option2)
                    }
                }

                holder.checkBoxThree.setOnCheckedChangeListener { buttonView, isChecked ->
                    examModel.givenAnswer = if (holder.checkBoxThree.isChecked) {
                        addAnswer(examModel.givenAnswer, examModel.content.option3)
                    } else {
                        removeAnswer(examModel.givenAnswer, examModel.content.option3)
                    }
                }

                holder.checkBoxFour.setOnCheckedChangeListener { buttonView, isChecked ->
                    examModel.givenAnswer = if (holder.checkBoxFour.isChecked) {
                        addAnswer(examModel.givenAnswer, examModel.content.option4)
                    } else {
                        removeAnswer(examModel.givenAnswer, examModel.content.option4)
                    }
                }
            }
        }
    }

    private fun removeAnswer(givenAnswer: String, optionString: String): String {
        if (givenAnswer.isEmpty())
            return ""
        else {
            if (givenAnswer.contains(";")) {
                val parts = givenAnswer.split(";")

                var answerString = ""
                for (part in parts) {
                    if (part != optionString) {
                        answerString += if (answerString.isEmpty()) {
                            part
                        } else {
                            ";$part"
                        }
                    }
                }

                return answerString
            } else {
                return if (givenAnswer == optionString)
                    ""
                else
                    givenAnswer
            }
        }
    }

    private fun addAnswer(givenAnswer: String, optionString: String): String {
        if (givenAnswer.isEmpty())
            return optionString
        else {
            if (givenAnswer.contains(";")) {
                val parts = givenAnswer.split(";")

                for (part in parts) {
                    if (part == optionString) {
                        return givenAnswer
                    }
                }

                return "$givenAnswer;$optionString"
            } else {
                return if (givenAnswer == optionString)
                    givenAnswer
                else
                    "$givenAnswer;$optionString"
            }
        }
    }
}